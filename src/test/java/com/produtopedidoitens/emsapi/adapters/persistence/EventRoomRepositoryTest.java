package com.produtopedidoitens.emsapi.adapters.persistence;

import com.produtopedidoitens.emsapi.adapters.configuration.QueryDslConfiguration;
import com.produtopedidoitens.emsapi.application.domain.entities.EventRoomEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDslConfiguration.class)
class EventRoomRepositoryTest {

    @Autowired
    private EventRoomRepository eventRoomRepository;

    // Inicia e finaliza transações manualmente, garantindo que cada operação seja executada em uma nova transação.
    @Autowired
    private PlatformTransactionManager transactionManager;

    private EventRoomEntity eventRoomEntity;
    private EventRoomEntity entitySaved;
    private List<EventRoomEntity> eventRoomEntities = new ArrayList<>();

    @BeforeEach
    void setUp() {
        eventRoomRepository.deleteAll();

        eventRoomEntity = EventRoomEntity.builder().roomName("Room 1").capacity(10).isFull(false).build();
        EventRoomEntity event2 = EventRoomEntity.builder().roomName("Room 2").capacity(20).isFull(false).build();
        EventRoomEntity event3 = EventRoomEntity.builder().roomName("Room 3").capacity(30).isFull(false).build();
        EventRoomEntity event4 = EventRoomEntity.builder().roomName("Room 4").capacity(30).isFull(false).build();

        eventRoomEntities.add(eventRoomEntity);
        eventRoomEntities.add(event2);
        eventRoomEntities.add(event3);
        eventRoomEntities.add(event4);

        entitySaved = assertDoesNotThrow(() -> eventRoomRepository.save(eventRoomEntity));
        assertDoesNotThrow(() -> eventRoomRepository.save(event2));
        assertDoesNotThrow(() -> eventRoomRepository.save(event3));
        assertDoesNotThrow(() -> eventRoomRepository.save(event4));
    }

    @Test
    void testFindById() {
        Optional<EventRoomEntity> eventRetrieved = assertDoesNotThrow(() -> eventRoomRepository
                .findById(entitySaved.getEventRoomId()));
        assertNotNull(eventRetrieved);

        assertEquals(entitySaved.getEventRoomId(), eventRetrieved.get().getEventRoomId());
        assertEquals(entitySaved.getRoomName(), eventRetrieved.get().getRoomName());
        assertEquals(entitySaved.getCapacity(), eventRetrieved.get().getCapacity());
        assertEquals(entitySaved.isFull(), eventRetrieved.get().isFull());
    }

    @Test
    void testFindAll() {
        List<EventRoomEntity> eventsRetrieved = assertDoesNotThrow(() -> eventRoomRepository.findAll());

        assertNotNull(eventsRetrieved);
        assertEquals(4, eventsRetrieved.size());

        for (int i = 0; i < eventsRetrieved.size(); i++) {
            assertEquals(eventRoomEntities.get(i).getEventRoomId(), eventsRetrieved.get(i).getEventRoomId());
            assertEquals(eventRoomEntities.get(i).getRoomName(), eventsRetrieved.get(i).getRoomName());
            assertEquals(eventRoomEntities.get(i).getCapacity(), eventsRetrieved.get(i).getCapacity());
            assertEquals(eventRoomEntities.get(i).isFull(), eventsRetrieved.get(i).isFull());
        }
    }

    @Test
    void testSimultaneousReadAndWriteWithoutConflict() {
        EventRoomEntity room = EventRoomEntity.builder().roomName("Room A").capacity(100).isFull(false).build();
        eventRoomRepository.save(room);

        EventRoomEntity readRoom1 = eventRoomRepository.findByRoomName("Room A");
        readRoom1.setCapacity(200);
        eventRoomRepository.save(readRoom1);

        EventRoomEntity readRoom2 = eventRoomRepository.findByRoomName("Room A");
        assertEquals(200, readRoom2.getCapacity());
    }

    @Test
    void testUpdateConflict() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // Garante que cada operação de leitura e escrita seja realizada em uma nova transação separada.
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        // Iniciar transação principal
        TransactionStatus status = transactionManager.getTransaction(def);

        EventRoomEntity room = EventRoomEntity.builder().roomName("Room B").capacity(100).isFull(false).build();
        eventRoomRepository.save(room);
        log.info("Objeto salvo pela transação principal{}", room);

        // Realizamos commit manualmente após cada operação para garantir que as alterações sejam persistidas corretamente
        // e o lock otimista seja aplicado conforme esperado.
        transactionManager.commit(status);

        // Simulando duas leituras da mesma entidade em transações separadas
        EventRoomEntity readRoom1 = eventRoomRepository.findByRoomName("Room B");
        log.info("Objeto lido pela transação secundária: {}", readRoom1);

        status = transactionManager.getTransaction(def); // Inicia transação
        EventRoomEntity readRoom2 = eventRoomRepository.findByRoomName("Room B");
        log.info("Objeto lido pela transação secundária: {}", readRoom2);

        transactionManager.commit(status);

        // Atualizando a entidade na primeira transação
        status = transactionManager.getTransaction(def); // Inicia transação

        readRoom1.setCapacity(200);
        eventRoomRepository.save(readRoom1);
        log.info("Objeto atualizado pela transação secundária: {}", readRoom1);

        transactionManager.commit(status);

        // Tentando atualizar a entidade na segunda transação
        status = transactionManager.getTransaction(def);
        readRoom2.setCapacity(300);
        log.info("Objeto atualizado pela transação secundária: {}", readRoom2);

        TransactionStatus finalStatus = status;

        Exception exception = assertThrows(Exception.class, () -> {
            eventRoomRepository.save(readRoom2);
            transactionManager.commit(finalStatus);
        });

        assertInstanceOf(ObjectOptimisticLockingFailureException.class, exception, "Esperava uma exceção de bloqueio otimista");
        log.info("Exception capturada: {}", exception.getMessage());
    }

    @Test
    void testFindByRoomNameSimultaneousReadAndWriteWithoutConflictQueryDSL() {
        EventRoomEntity room = EventRoomEntity.builder().roomName("Room A").capacity(100).isFull(false).build();
        assertDoesNotThrow(() -> eventRoomRepository.save(room));

        EventRoomEntity readRoom1 = eventRoomRepository.findByRoomNameWithOptimistickLock("Room A");
        readRoom1.setCapacity(200);
        assertDoesNotThrow(() -> eventRoomRepository.save(readRoom1));

        EventRoomEntity readRoom2 = eventRoomRepository.findByRoomNameWithOptimistickLock("Room A");
        assertEquals(200, readRoom2.getCapacity());
    }

    @Test
    void testUpdateConflictFindByRoomNameSimultaneousReadAndWriteWithoutConflictQueryDSL() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        TransactionStatus status = transactionManager.getTransaction(def);
        EventRoomEntity room = EventRoomEntity.builder().roomName("Room C").capacity(100).isFull(false).build();
        eventRoomRepository.save(room);
        transactionManager.commit(status);

        EventRoomEntity readRoom1 = eventRoomRepository.findByRoomNameWithOptimistickLock("Room C");

        status = transactionManager.getTransaction(def);

        EventRoomEntity readRoom2 = eventRoomRepository.findByRoomNameWithOptimistickLock("Room C");
        transactionManager.commit(status);

        status = transactionManager.getTransaction(def);

        readRoom1.setCapacity(200);
        eventRoomRepository.save(readRoom1);
        transactionManager.commit(status);

        status = transactionManager.getTransaction(def);
        readRoom2.setCapacity(300);

        TransactionStatus finalStatus = status;

        Exception exception = assertThrows(Exception.class, () -> {
            eventRoomRepository.save(readRoom2);
            transactionManager.commit(finalStatus);
        });

        assertInstanceOf(ObjectOptimisticLockingFailureException.class, exception, "Esperava uma exceção de bloqueio otimista");
        log.info("Exception capturada: {}", exception.getMessage());
    }

}
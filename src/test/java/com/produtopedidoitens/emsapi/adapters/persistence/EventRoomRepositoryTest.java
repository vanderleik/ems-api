package com.produtopedidoitens.emsapi.adapters.persistence;

import com.produtopedidoitens.emsapi.application.domain.entities.EventRoomEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
class EventRoomRepositoryTest {

    @Autowired
    private EventRoomRepository eventRoomRepository;

    // inicia e finaliza transações manualmente, garantindo que cada operação seja executada em uma nova transação.
    @Autowired
    private PlatformTransactionManager transactionManager;

    private EventRoomEntity eventRoomEntity;
    private EventRoomEntity entitySaved;
    private List<EventRoomEntity> eventRoomEntities = new ArrayList<>();

    @BeforeEach
    void setUp() {
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
        log.info("Entity retrieved: {}", eventRetrieved);

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
        EventRoomEntity room = EventRoomEntity.builder()
                .roomName("Room A")
                .capacity(100)
                .isFull(false)
                .build();
        eventRoomRepository.save(room);

        //Simulando a leitura da entidade
        EventRoomEntity readRoom1 = eventRoomRepository.findByRoomName("Room A");

        //Atualizando a entidade
        readRoom1.setCapacity(200);
        eventRoomRepository.save(readRoom1);

        //Ler novamente para verificar se a atualização foi bem sucedida
        EventRoomEntity readRoom2 = eventRoomRepository.findByRoomName("Room A");
        assertEquals(200, readRoom2.getCapacity());
    }

    @Test
    void testUpdateConflict() {
        // Iniciar transação principal
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // Garante que cada operação de leitura e escrita seja realizada em uma nova transação separada.
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);

        EventRoomEntity room = EventRoomEntity.builder()
                .roomName("Room B")
                .capacity(100)
                .isFull(false)
                .build();
        eventRoomRepository.save(room);

        // Realizamos commit manualmente após cada operação para garantir que as alterações sejam persistidas corretamente
        // e o lock otimista seja aplicado conforme esperado.
        transactionManager.commit(status);

        // Simulando duas leituras da mesma entidade em transações separadas
        EventRoomEntity readRoom1 = eventRoomRepository.findByRoomName("Room B");

        status = transactionManager.getTransaction(def);
        EventRoomEntity readRoom2 = eventRoomRepository.findByRoomName("Room B");
        transactionManager.commit(status);

        // Atualizando a entidade na primeira transação
        status = transactionManager.getTransaction(def);
        readRoom1.setCapacity(200);
        eventRoomRepository.save(readRoom1);
        transactionManager.commit(status);

        // Tentando atualizar a entidade na segunda transação
        status = transactionManager.getTransaction(def);
        readRoom2.setCapacity(300);

        TransactionStatus finalStatus = status;
        Exception exception = assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            eventRoomRepository.save(readRoom2);
            transactionManager.commit(finalStatus);
        });
        assertTrue(exception instanceof ObjectOptimisticLockingFailureException, "Esperava uma exceção de bloqueio otimista");
    }

}
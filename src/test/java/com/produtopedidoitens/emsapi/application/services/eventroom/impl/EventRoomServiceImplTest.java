package com.produtopedidoitens.emsapi.application.services.eventroom.impl;

import com.produtopedidoitens.emsapi.application.validators.eventroom.EventRoomValidator;
import com.produtopedidoitens.emsapi.domain.entities.EventRoomEntity;
import com.produtopedidoitens.emsapi.persistence.EventRoomRepository;
import com.produtopedidoitens.emsapi.utils.MessagesConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventRoomServiceImplTest {

    @InjectMocks
    private EventRoomServiceImpl eventRoomService;

    @Mock
    private EventRoomRepository eventRoomRepository;

    @Mock
    private EventRoomValidator eventRoomValidator;

    private EventRoomEntity eventRoomEntity;

    @BeforeEach
    void setUp() {
        eventRoomEntity = EventRoomEntity.builder()
                .eventRoomId(UUID.fromString("d503a3dc-d1c0-4647-94ce-2d66ed4b6a04"))
                .roomName("Room 1")
                .capacity(10)
                .isFull(false)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0)
                .build();
    }

    @Test
    @DisplayName("Deve salvar uma sala de eventos")
    void testSaveEventRoom() {
        when(eventRoomRepository.save(eventRoomEntity)).thenReturn(eventRoomEntity);
        EventRoomEntity result = assertDoesNotThrow(() -> eventRoomService.saveEventRoom(eventRoomEntity));

        assertNotNull(result);
        assertEquals(eventRoomEntity.getRoomName(), result.getRoomName());
        assertEquals(eventRoomEntity.getCapacity(), result.getCapacity());
        assertEquals(eventRoomEntity.isFull(), result.isFull());
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar salvar uma sala de eventos com capacidade cheia")
    void testSaveEventRoomFull() {
        eventRoomEntity = EventRoomEntity.builder()
                .eventRoomId(UUID.fromString("d503a3dc-d1c0-4647-94ce-2d66ed4b6a05"))
                .roomName("Room 1")
                .capacity(10)
                .isFull(true)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0)
                .build();

        Exception exception = assertThrows(Exception.class, () -> eventRoomService.saveEventRoom(eventRoomEntity));
        assertEquals(MessagesConstants.ROOM_IS_FULL, exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar uma sala de eventos pelo id")
    void testFindEventRoomById() {
        when(eventRoomRepository.findById(eventRoomEntity.getEventRoomId())).thenReturn(Optional.of(eventRoomEntity));
        EventRoomEntity result = assertDoesNotThrow(() -> eventRoomService.findEventRoomById(eventRoomEntity.getEventRoomId().toString()));

        assertNotNull(result);
        assertEquals(eventRoomEntity.getRoomName(), result.getRoomName());
        assertEquals(eventRoomEntity.getCapacity(), result.getCapacity());
        assertEquals(eventRoomEntity.isFull(), result.isFull());
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar uma sala de eventos pelo id")
    void testFindEventRoomByIdError() {
        Exception exception = assertThrows(Exception.class, () -> eventRoomService.findEventRoomById(eventRoomEntity.getEventRoomId().toString()));

        assertEquals(MessagesConstants.ROOM_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar todas as salas de eventos")
    void testFindAllEventRooms() {
        when(eventRoomRepository.findAll()).thenReturn(List.of(eventRoomEntity));

        List<EventRoomEntity> entityList = assertDoesNotThrow(() -> eventRoomService.findAllEventRooms());
        assertNotNull(entityList);
        assertFalse(entityList.isEmpty());
        assertEquals(1, entityList.size());
        assertEquals(eventRoomEntity.getRoomName(), entityList.getFirst().getRoomName());
        assertEquals(eventRoomEntity.getCapacity(), entityList.getFirst().getCapacity());
        assertEquals(eventRoomEntity.isFull(), entityList.getFirst().isFull());
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar todas as salas de eventos")
    void testFindAllEventRoomsError() {
        when(eventRoomRepository.findAll()).thenReturn(List.of());

        Exception exception = assertThrows(Exception.class, () -> eventRoomService.findAllEventRooms());
        assertEquals(MessagesConstants.ROOM_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar uma sala de eventos pelo nome")
    void testFindByRoomName() {
        when(eventRoomRepository.findByRoomName(eventRoomEntity.getRoomName())).thenReturn(eventRoomEntity);

        EventRoomEntity result = assertDoesNotThrow(() -> eventRoomService.findByRoomName(eventRoomEntity.getRoomName()));
        assertNotNull(result);
        assertEquals(eventRoomEntity.getRoomName(), result.getRoomName());
        assertEquals(eventRoomEntity.getCapacity(), result.getCapacity());
        assertEquals(eventRoomEntity.isFull(), result.isFull());
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar uma sala de eventos pelo nome")
    void testFindByRoomNameError() {
        when(eventRoomRepository.findByRoomName(eventRoomEntity.getRoomName())).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> eventRoomService.findByRoomName(eventRoomEntity.getRoomName()));
        assertEquals(MessagesConstants.ROOM_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar uma sala de eventos pelo nome com lock otimista")
    void testFindByRoomNameWithOptimistickLock() {
        when(eventRoomRepository.findByRoomNameWithOptimistickLock(eventRoomEntity.getRoomName())).thenReturn(eventRoomEntity);

        EventRoomEntity result = assertDoesNotThrow(() -> eventRoomService.findByRoomNameWithOptimistickLock(eventRoomEntity.getRoomName()));
        assertNotNull(result);
        assertEquals(eventRoomEntity.getRoomName(), result.getRoomName());
        assertEquals(eventRoomEntity.getCapacity(), result.getCapacity());
        assertEquals(eventRoomEntity.isFull(), result.isFull());
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar uma sala de eventos pelo nome com lock otimista")
    void testFindByRoomNameWithOptimistickLockError() {
        when(eventRoomRepository.findByRoomNameWithOptimistickLock(eventRoomEntity.getRoomName())).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> eventRoomService.findByRoomNameWithOptimistickLock(eventRoomEntity.getRoomName()));
        assertEquals(MessagesConstants.ROOM_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar uma sala de eventos")
    void testUpdateEventRoom() {
        when(eventRoomRepository.findById(eventRoomEntity.getEventRoomId())).thenReturn(Optional.of(eventRoomEntity));
        when(eventRoomRepository.save(eventRoomEntity)).thenReturn(eventRoomEntity);

        EventRoomEntity result = assertDoesNotThrow(() -> eventRoomService.updateEventRoom(eventRoomEntity, eventRoomEntity.getEventRoomId().toString()));
        assertNotNull(result);
        assertEquals(eventRoomEntity.getRoomName(), result.getRoomName());
        assertEquals(eventRoomEntity.getCapacity(), result.getCapacity());
        assertEquals(eventRoomEntity.isFull(), result.isFull());
    }

    @Test
    @DisplayName("Deve retornar um erro ao atualizar uma sala de eventos não encontrada")
    void testUpdateEventRoomNotFound() {
        when(eventRoomRepository.findById(eventRoomEntity.getEventRoomId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> eventRoomService.updateEventRoom(eventRoomEntity, eventRoomEntity.getEventRoomId().toString()));
        assertEquals(MessagesConstants.ROOM_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar uma sala de eventos pelo id")
    void testDeleteEventRoomById() {
        when(eventRoomRepository.findById(eventRoomEntity.getEventRoomId())).thenReturn(Optional.of(eventRoomEntity));

        assertDoesNotThrow(() -> eventRoomService.deleteEventRoomById(eventRoomEntity.getEventRoomId().toString()));
    }


    @Test
    @DisplayName("Deve retornar um erro ao deletar uma sala de eventos não encontrada")
    void testDeleteEventRoomByIdError() {
        when(eventRoomRepository.findById(eventRoomEntity.getEventRoomId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> eventRoomService.deleteEventRoomById(eventRoomEntity.getEventRoomId().toString()));
        assertEquals(MessagesConstants.ROOM_NOT_FOUND, exception.getMessage());
    }

}

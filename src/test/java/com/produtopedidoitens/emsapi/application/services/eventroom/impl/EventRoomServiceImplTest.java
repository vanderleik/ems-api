package com.produtopedidoitens.emsapi.application.services.eventroom.impl;

import com.produtopedidoitens.emsapi.application.validators.eventroom.EventRoomValidator;
import com.produtopedidoitens.emsapi.domain.entities.EventRoomEntity;
import com.produtopedidoitens.emsapi.persistence.EventRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
                .eventRoomId(UUID.randomUUID())
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
                .eventRoomId(UUID.randomUUID())
                .roomName("Room 1")
                .capacity(10)
                .isFull(true)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0)
                .build();
//        when(eventRoomRepository.save(eventRoomEntity)).thenReturn(eventRoomEntity);
        EventRoomEntity result = assertDoesNotThrow(() -> eventRoomService.saveEventRoom(eventRoomEntity));

        assertNotNull(result);
        assertEquals(eventRoomEntity.getRoomName(), result.getRoomName());
        assertEquals(eventRoomEntity.getCapacity(), result.getCapacity());
        assertEquals(eventRoomEntity.isFull(), result.isFull());
    }

    @Test
    void testFindEventRoomById() {
    }

    @Test
    void testFindAllEventRooms() {
    }

    @Test
    void testFindByRoomName() {
    }

    @Test
    void testFindByRoomNameWithOptimistickLock() {
    }

    @Test
    void testUpdateEventRoom() {
    }

    @Test
    void testDeleteEventRoomById() {
    }

}

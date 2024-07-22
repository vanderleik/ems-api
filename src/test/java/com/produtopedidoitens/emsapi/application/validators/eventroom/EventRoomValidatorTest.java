package com.produtopedidoitens.emsapi.application.validators.eventroom;

import com.produtopedidoitens.emsapi.domain.entities.EventRoomEntity;
import com.produtopedidoitens.emsapi.utils.MessagesConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EventRoomValidatorTest {

    @InjectMocks
    private EventRoomValidator eventRoomValidator;

    private EventRoomEntity eventRoomEntity;

    @Test
    void testValidateRoomNameNull() {
        eventRoomEntity = EventRoomEntity.builder()
                .eventRoomId(UUID.randomUUID())
                .roomName(null)
                .capacity(10)
                .isFull(false)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0)
                .build();

        Exception exception = assertThrows(Exception.class, () -> eventRoomValidator.validate(eventRoomEntity));
        assertEquals(MessagesConstants.ROOM_NAME_CANNOT_BE_NULL_OR_EMPTY, exception.getMessage());
    }

    @Test
    void testValidateRoomNameEmpty() {
        eventRoomEntity = EventRoomEntity.builder()
                .eventRoomId(UUID.randomUUID())
                .roomName("")
                .capacity(10)
                .isFull(false)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0)
                .build();

        Exception exception = assertThrows(Exception.class, () -> eventRoomValidator.validate(eventRoomEntity));
        assertEquals(MessagesConstants.ROOM_NAME_CANNOT_BE_NULL_OR_EMPTY, exception.getMessage());
    }

    @Test
    void testValidateCapacityLessThanZero() {
        eventRoomEntity = EventRoomEntity.builder()
                .eventRoomId(UUID.randomUUID())
                .roomName("Room 1")
                .capacity(-1)
                .isFull(false)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0)
                .build();

        Exception exception = assertThrows(Exception.class, () -> eventRoomValidator.validate(eventRoomEntity));
        assertEquals(MessagesConstants.CAPACITY_CANNOT_BE_LESS_THAN_OR_EQUAL_TO_ZERO, exception.getMessage());
    }

}

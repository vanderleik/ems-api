package com.produtopedidoitens.emsapi.application.validators.eventroom;

import com.produtopedidoitens.emsapi.application.services.exceptions.eventroom.EventRoomIllegalArgumentException;
import com.produtopedidoitens.emsapi.domain.entities.EventRoomEntity;
import com.produtopedidoitens.emsapi.utils.MessagesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventRoomValidator {

    public void validate(EventRoomEntity eventRoomEntity) {
        validateRoomName(eventRoomEntity.getRoomName());
        validateCapacity(eventRoomEntity.getCapacity());
    }

    private void validateRoomName(String roomName) {
        if (roomName == null || roomName.isBlank()) {
            log.error("validateRoomName:: {}", MessagesConstants.ROOM_NAME_CANNOT_BE_NULL_OR_EMPTY);
            throw new EventRoomIllegalArgumentException(MessagesConstants.ROOM_NAME_CANNOT_BE_NULL_OR_EMPTY);
        }
    }

    private void validateCapacity(int capacity) {
        if (capacity <= 0) {
            log.error("validateCapacity:: {}", MessagesConstants.CAPACITY_CANNOT_BE_LESS_THAN_OR_EQUAL_TO_ZERO);
            throw new EventRoomIllegalArgumentException(MessagesConstants.CAPACITY_CANNOT_BE_LESS_THAN_OR_EQUAL_TO_ZERO);
        }
    }

}

package com.produtopedidoitens.emsapi.application.converters;

import com.produtopedidoitens.emsapi.controllers.dtos.EventRoomRequest;
import com.produtopedidoitens.emsapi.controllers.dtos.EventRoomRequestToUpdate;
import com.produtopedidoitens.emsapi.controllers.dtos.EventRoomResponse;
import com.produtopedidoitens.emsapi.domain.entities.EventRoomEntity;
import org.springframework.stereotype.Component;

@Component
public class EventRoomConverter {

    public EventRoomEntity convertEventRoomRequestToEntity(EventRoomRequest eventRoomRequest) {
        return EventRoomEntity.builder()
                .roomName(eventRoomRequest.roomName())
                .capacity(eventRoomRequest.capacity())
                .isFull(Boolean.FALSE)
                .build();
    }

    public EventRoomResponse convertEventRoomEntityToResponse(EventRoomEntity eventRoomEntity) {
        return EventRoomResponse.builder()
                .eventRoomId(eventRoomEntity.getEventRoomId())
                .roomName(eventRoomEntity.getRoomName())
                .capacity(eventRoomEntity.getCapacity())
                .isFull(eventRoomEntity.isFull())
                .dthreg(eventRoomEntity.getDthreg())
                .dthalt(eventRoomEntity.getDthalt())
                .version(eventRoomEntity.getVersion())
                .build();
    }

    public EventRoomEntity convertEventRoomRequestUpdateToEntity(EventRoomRequestToUpdate eventRoomRequestToUpdate) {
        return EventRoomEntity.builder()
                .roomName(eventRoomRequestToUpdate.roomName())
                .capacity(eventRoomRequestToUpdate.capacity())
                .build();
    }
}

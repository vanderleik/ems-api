package com.produtopedidoitens.emsapi.controllers.dtos;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record EventRoomResponse(

        UUID eventRoomId,
        String roomName,
        int capacity,
        boolean isFull,
        LocalDateTime dthreg,
        LocalDateTime dthalt,
        int version

) {
}

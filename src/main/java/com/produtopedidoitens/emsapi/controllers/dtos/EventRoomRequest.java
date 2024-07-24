package com.produtopedidoitens.emsapi.controllers.dtos;

import lombok.Builder;

@Builder
public record EventRoomRequest(

        String roomName,
        int capacity

) {
}

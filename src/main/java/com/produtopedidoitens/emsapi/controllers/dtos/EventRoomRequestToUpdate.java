package com.produtopedidoitens.emsapi.controllers.dtos;

import lombok.Builder;

@Builder
public record EventRoomRequestToUpdate(

        String roomName,
        int capacity

) {
}

package com.produtopedidoitens.emsapi.application.services.exceptions.eventroom;

public class EventRoomIllegalArgumentException extends RuntimeException {

    public EventRoomIllegalArgumentException(String message) {
        super(message);
    }

    public EventRoomIllegalArgumentException(String message, String roomName) {
        super(String.format(message, roomName));
    }
}

package com.produtopedidoitens.emsapi.utils;

public class MessagesConstants {

    public static final String ROOM_NAME_CANNOT_BE_NULL_OR_EMPTY = "O nome da sala de eventos não pode ser nulo ou vazio";
    public static final String CAPACITY_CANNOT_BE_LESS_THAN_OR_EQUAL_TO_ZERO = "A capacidade da sala de eventos não pode ser menor ou igual a zero";
    public static final String ROOM_IS_FULL = "Sala de eventos cheia";

    private MessagesConstants() {
    }

    public static MessagesConstants createMessagesConstants() {
        return new MessagesConstants();
    }

}

package com.produtopedidoitens.emsapi.application.services.eventroom;

import com.produtopedidoitens.emsapi.domain.entities.EventRoomEntity;

import java.util.List;

public interface EventRoomService {

    EventRoomEntity saveEventRoom(EventRoomEntity eventRoomEntity);
    EventRoomEntity findEventRoomById(String eventRoomId);
    List<EventRoomEntity> findAllEventRooms();
    EventRoomEntity findByRoomName(String roomName);
    EventRoomEntity findByRoomNameWithOptimistickLock(String roomName);
    EventRoomEntity updateEventRoom(EventRoomEntity eventRoomEntity, String eventRoomId);
    void deleteEventRoomById(String eventRoomId);

}

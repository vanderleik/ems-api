package com.produtopedidoitens.emsapi.application.services.eventroom.impl;

import com.produtopedidoitens.emsapi.application.services.eventroom.EventRoomService;
import com.produtopedidoitens.emsapi.application.services.exceptions.eventroom.EventRoomIllegalArgumentException;
import com.produtopedidoitens.emsapi.application.validators.eventroom.EventRoomValidator;
import com.produtopedidoitens.emsapi.domain.entities.EventRoomEntity;
import com.produtopedidoitens.emsapi.persistence.EventRoomRepository;
import com.produtopedidoitens.emsapi.utils.MessagesConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventRoomServiceImpl implements EventRoomService {

    private final EventRoomRepository eventRoomRepository;
    private final EventRoomValidator eventRoomValidator;

    @Override
    public EventRoomEntity saveEventRoom(EventRoomEntity eventRoomEntity) {
        log.info("saveEventRoom:: Recebendo requisição para salvar sala de eventos");
        eventRoomValidator.validate(eventRoomEntity);

        eventRoomIsFull(eventRoomEntity.isFull());
        return eventRoomRepository.save(eventRoomEntity);
    }

    private static void eventRoomIsFull(boolean isFull) {
        log.info("eventRoomIsFull:: Verificando se a sala de eventos está cheia");

        if(Boolean.TRUE.equals(isFull)) {
            log.error("saveEventRoom:: {}", MessagesConstants.ROOM_IS_FULL);
            throw new EventRoomIllegalArgumentException(MessagesConstants.ROOM_IS_FULL);
        }

    }

    @Override
    public EventRoomEntity findEventRoomById(String id) {
        return null;
    }

    @Override
    public List<EventRoomEntity> findAllEventRooms() {
        return List.of();
    }

    @Override
    public EventRoomEntity findByRoomName(String roomName) {
        return null;
    }

    @Override
    public EventRoomEntity findByRoomNameWithOptimistickLock(String roomName) {
        return null;
    }

    @Override
    public EventRoomEntity updateEventRoom(EventRoomEntity eventRoomEntity) {
        return null;
    }

    @Override
    public void deleteEventRoomById(String id) {

    }
}

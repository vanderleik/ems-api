package com.produtopedidoitens.emsapi.application.services.eventroom.impl;

import com.produtopedidoitens.emsapi.application.services.eventroom.EventRoomService;
import com.produtopedidoitens.emsapi.application.services.exceptions.eventroom.EventRoomIllegalArgumentException;
import com.produtopedidoitens.emsapi.application.services.exceptions.eventroom.EventRoomNotFoundException;
import com.produtopedidoitens.emsapi.application.validators.eventroom.EventRoomValidator;
import com.produtopedidoitens.emsapi.domain.entities.EventRoomEntity;
import com.produtopedidoitens.emsapi.persistence.EventRoomRepository;
import com.produtopedidoitens.emsapi.utils.MessagesConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventRoomServiceImpl implements EventRoomService {

    private final EventRoomRepository eventRoomRepository;
    private final EventRoomValidator eventRoomValidator;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public EventRoomEntity saveEventRoom(EventRoomEntity eventRoomEntity) {
        log.info("saveEventRoom:: Recebendo requisição para salvar sala de eventos");
        eventRoomValidator.validate(eventRoomEntity);
        eventRoomExistsByEventRoomName(eventRoomEntity.getRoomName());

        eventRoomIsFull(eventRoomEntity.isFull());
        EventRoomEntity eventRoom = eventRoomRepository.save(eventRoomEntity);
        log.info("saveEventRoom:: Salvando sala de eventos: {}", eventRoom);
        return eventRoom;
    }

    @Override
    public EventRoomEntity findEventRoomById(String eventRoomId) {
        log.info("findEventRoomById:: Buscando sala de eventos por id: {}", eventRoomId);
        EventRoomEntity eventRoom = getEventRoomById(eventRoomId);
        log.info("findEventRoomById:: Sala de eventos encontrada: {}", eventRoom);
        return eventRoom;
    }

    @Override
    public List<EventRoomEntity> findAllEventRooms() {
        log.info("findAllEventRooms:: Buscando todas as salas de eventos");
        List<EventRoomEntity> eventRooms = getEventRoomList();

        log.info("findAllEventRooms:: Salas de eventos encontradas: {}", eventRooms);
        return eventRooms;
    }


    @Transactional
    @Override
    public EventRoomEntity findByRoomName(String roomName) {
        log.info("findByRoomName:: Buscando sala de eventos por nome: {}", roomName);
        EventRoomEntity eventRoom = getEventRoomByName(roomName);

        log.info("findByRoomName:: Sala de eventos encontrada: {}", eventRoom);
        return eventRoom;
    }


    @Transactional
    @Override
    public EventRoomEntity findByRoomNameWithOptimistickLock(String roomName) {
        log.info("findByRoomNameWithOptimistickLock:: Buscando sala de eventos por nome com lock otimista: {}", roomName);
        EventRoomEntity eventRoom = getRoomByNameWithCustomMethod(roomName);

        log.info("findByRoomNameWithOptimistickLock:: Sala de eventos encontrada: {}", eventRoom);
        return eventRoom;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public EventRoomEntity updateEventRoom(EventRoomEntity eventRoomEntity, String eventRoomId) {
        log.info("updateEventRoom:: Recebendo requisição para atualizar sala de eventos");

        EventRoomEntity eventRoomUpdated = updateEventRoomWithData(eventRoomEntity, eventRoomId);
        log.info("updateEventRoom:: Sala de eventos atualizada: {}", eventRoomUpdated);
        return eventRoomUpdated;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteEventRoomById(String eventRoomId) {
        log.info("deleteEventRoomById:: Recebendo requisição para deletar sala de eventos por id: {}", eventRoomId);

        EventRoomEntity eventRoom = getEventRoomById(eventRoomId);
        eventRoomRepository.delete(eventRoom);
    }

    private EventRoomEntity updateEventRoomWithData(EventRoomEntity eventRoomEntity, String eventRoomId) {
        EventRoomEntity eventRoom = getEventRoomById(eventRoomId);

        eventRoom.setRoomName(eventRoomEntity.getRoomName() == null ? eventRoom.getRoomName() : eventRoomEntity.getRoomName());
        eventRoom.setCapacity(eventRoomEntity.getCapacity() == 0  ? eventRoom.getCapacity() : eventRoomEntity.getCapacity());

        return eventRoomRepository.save(eventRoom);
    }

    private EventRoomEntity getRoomByNameWithCustomMethod(String roomName) {
        EventRoomEntity eventRoom = eventRoomRepository.findByRoomNameWithOptimistickLock(roomName);
        if (eventRoom == null) {
            log.error("findByRoomNameWithOptimistickLock:: {}", MessagesConstants.ROOM_NOT_FOUND);
            throw new EventRoomNotFoundException(MessagesConstants.ROOM_NOT_FOUND);
        }

        return eventRoom;
    }

    private EventRoomEntity getEventRoomByName(String roomName) {
        EventRoomEntity eventRoom = eventRoomRepository.findByRoomName(roomName);

        if (eventRoom == null) {
            log.error("findByRoomName:: {}", MessagesConstants.ROOM_NOT_FOUND);
            throw new EventRoomNotFoundException(MessagesConstants.ROOM_NOT_FOUND);
        }
        return eventRoom;
    }

    private List<EventRoomEntity> getEventRoomList() {
        List<EventRoomEntity> eventRooms = eventRoomRepository.findAll();

        if (eventRooms.isEmpty()) {
            log.error("findAllEventRooms:: {}", MessagesConstants.ROOM_NOT_FOUND);
            throw new EventRoomNotFoundException(MessagesConstants.ROOM_NOT_FOUND);
        }
        return eventRooms;
    }

    private EventRoomEntity getEventRoomById(String eventRoomId) {
        return eventRoomRepository.findById(UUID.fromString(eventRoomId)).
                orElseThrow(() -> {
                    log.error("findEventRoomById:: {}", MessagesConstants.ROOM_NOT_FOUND);
                    return new EventRoomNotFoundException(MessagesConstants.ROOM_NOT_FOUND);
                });
    }

    private void eventRoomIsFull(boolean isFull) {
        log.info("eventRoomIsFull:: Verificando se a sala de eventos está cheia");

        if(Boolean.TRUE.equals(isFull)) {
            log.error("saveEventRoom:: {}", MessagesConstants.ROOM_IS_FULL);
            throw new EventRoomIllegalArgumentException(MessagesConstants.ROOM_IS_FULL);
        }
    }

    private void eventRoomExistsByEventRoomName(String roomName) {
        EventRoomEntity eventRoom = eventRoomRepository.findByRoomName(roomName);
        if (Objects.nonNull(eventRoom)) {
            log.error("saveEventRoom:: {} {}", MessagesConstants.ROOM_ALREADY_EXISTS, roomName);
            throw new EventRoomIllegalArgumentException(MessagesConstants.ROOM_ALREADY_EXISTS, roomName);
        }
    }

}

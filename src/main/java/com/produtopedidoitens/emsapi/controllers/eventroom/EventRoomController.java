package com.produtopedidoitens.emsapi.controllers.eventroom;

import com.produtopedidoitens.emsapi.application.converters.EventRoomConverter;
import com.produtopedidoitens.emsapi.application.services.eventroom.EventRoomService;
import com.produtopedidoitens.emsapi.controllers.dtos.EventRoomRequest;
import com.produtopedidoitens.emsapi.controllers.dtos.EventRoomRequestToUpdate;
import com.produtopedidoitens.emsapi.controllers.dtos.EventRoomResponse;
import com.produtopedidoitens.emsapi.domain.entities.EventRoomEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/eventrooms")
@RestController
public class EventRoomController {

    private final EventRoomService eventRoomService;
    private final EventRoomConverter eventRoomConverter;

    @PostMapping
    public ResponseEntity<EventRoomResponse> saveEventRoom(@Valid @RequestBody EventRoomRequest eventRoomRequest) {
        log.info("saveEventRoom:: Recebendo requisição para salvar sala de evento: {}", eventRoomRequest);
        EventRoomEntity eventRoomEntity = eventRoomConverter.convertEventRoomRequestToEntity(eventRoomRequest);
        EventRoomEntity eventRoomEntitySaved = eventRoomService.saveEventRoom(eventRoomEntity);

        return ResponseEntity.ok(eventRoomConverter.convertEventRoomEntityToResponse(eventRoomEntitySaved));
    }

    @GetMapping("/{eventRoomId}")
    public ResponseEntity<EventRoomResponse> findEventRoomById(@PathVariable String eventRoomId) {
        log.info("findEventRoomById:: Recebendo requisição para buscar sala de evento pelo id: {}", eventRoomId);
        EventRoomEntity eventRoomEntity = eventRoomService.findEventRoomById(eventRoomId);

        return ResponseEntity.ok(eventRoomConverter.convertEventRoomEntityToResponse(eventRoomEntity));
    }

    @GetMapping
    public ResponseEntity<List<EventRoomResponse>> findAllEventRooms() {
        log.info("findAllEventRooms:: Recebendo requisição para buscar todas as salas de eventos");
        List<EventRoomEntity> eventRoomEntities = eventRoomService.findAllEventRooms();

        return ResponseEntity.ok(eventRoomEntities.stream().map(eventRoomConverter::convertEventRoomEntityToResponse).toList());
    }

    @GetMapping("/roomName/{roomName}")
    public ResponseEntity<EventRoomResponse> findByRoomName(@PathVariable String roomName) {
        log.info("findByRoomName:: Recebendo requisição para buscar sala de evento pelo nome: {}", roomName);
        EventRoomEntity eventRoomEntity = eventRoomService.findByRoomName(roomName);

        return ResponseEntity.ok(eventRoomConverter.convertEventRoomEntityToResponse(eventRoomEntity));
    }

    @GetMapping("/roomNameWithOptimistickLock/{roomName}")
    public ResponseEntity<EventRoomResponse> findByRoomNameWithOptimistickLock(@PathVariable String roomName) {
        log.info("findByRoomNameWithOptimistickLock:: Recebendo requisição para buscar sala de evento pelo nome com lock otimista: {}", roomName);
        EventRoomEntity eventRoomEntity = eventRoomService.findByRoomNameWithOptimistickLock(roomName);

        return ResponseEntity.ok(eventRoomConverter.convertEventRoomEntityToResponse(eventRoomEntity));
    }

    @PutMapping("/{eventRoomId}")
    public ResponseEntity<EventRoomResponse> updateEventRoom(@PathVariable String eventRoomId, @Valid @RequestBody EventRoomRequestToUpdate eventRoomRequestToUpdate) {
        log.info("updateEventRoom:: Recebendo requisição para atualizar sala de evento pelo id: {}", eventRoomId);
        EventRoomEntity eventRoomEntity = eventRoomConverter.convertEventRoomRequestUpdateToEntity(eventRoomRequestToUpdate);
        EventRoomEntity eventRoomEntityUpdated = eventRoomService.updateEventRoom(eventRoomEntity, eventRoomId);

        return ResponseEntity.ok(eventRoomConverter.convertEventRoomEntityToResponse(eventRoomEntityUpdated));
    }

    @DeleteMapping("/{eventRoomId}")
    public ResponseEntity<Void> deleteEventRoomById(@PathVariable String eventRoomId) {
        log.info("deleteEventRoomById:: Recebendo requisição para deletar sala de evento pelo id: {}", eventRoomId);
        eventRoomService.deleteEventRoomById(eventRoomId);

        return ResponseEntity.noContent().build();
    }

}

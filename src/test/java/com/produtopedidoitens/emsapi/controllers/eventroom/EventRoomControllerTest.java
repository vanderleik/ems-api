package com.produtopedidoitens.emsapi.controllers.eventroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produtopedidoitens.emsapi.application.converters.EventRoomConverter;
import com.produtopedidoitens.emsapi.application.services.eventroom.EventRoomService;
import com.produtopedidoitens.emsapi.application.services.exceptions.eventroom.EventRoomIllegalArgumentException;
import com.produtopedidoitens.emsapi.application.services.exceptions.eventroom.EventRoomNotFoundException;
import com.produtopedidoitens.emsapi.controllers.dtos.EventRoomRequest;
import com.produtopedidoitens.emsapi.controllers.dtos.EventRoomRequestToUpdate;
import com.produtopedidoitens.emsapi.controllers.dtos.EventRoomResponse;
import com.produtopedidoitens.emsapi.domain.entities.EventRoomEntity;
import com.produtopedidoitens.emsapi.utils.MessagesConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = EventRoomController.class)
class EventRoomControllerTest {

    private final String URL = "/api/v1/eventrooms";
    private final String EVENT_ROOM_ID = "866ffb11-e58c-4dfe-9a97-14aaaca1b0eb";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private EventRoomService eventRoomService;
    @MockBean
    private EventRoomConverter eventRoomConverter;

    private EventRoomRequest eventRoomRequest;
    private EventRoomResponse eventRoomResponse;
    private EventRoomEntity eventRoomEntity;
    private EventRoomEntity eventRoomEntitySaved;

    @BeforeEach
    void setUp() {
        eventRoomRequest = EventRoomRequest.builder()
                .roomName("Sala Hard Rock")
                .capacity(10)
                .build();

        eventRoomEntity = EventRoomEntity.builder()
                .roomName("Sala Hard Rock")
                .capacity(10)
                .isFull(false)
                .build();

        eventRoomResponse = EventRoomResponse.builder()
                .eventRoomId(UUID.fromString(EVENT_ROOM_ID))
                .roomName("Sala Hard Rock")
                .capacity(10)
                .isFull(false)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0)
                .build();

        eventRoomEntitySaved = EventRoomEntity.builder()
                .eventRoomId(UUID.fromString(EVENT_ROOM_ID))
                .roomName("Sala Hard Rock")
                .capacity(10)
                .isFull(false)
                .dthreg(LocalDateTime.now())
                .dthalt(LocalDateTime.now())
                .version(0)
                .build();
    }

    @Test
    @DisplayName("Deve salvar uma sala de evento")
    void testSaveEventRoom() throws Exception {
        when(eventRoomConverter.convertEventRoomRequestToEntity(eventRoomRequest)).thenReturn(eventRoomEntity);
        when(eventRoomService.saveEventRoom(eventRoomEntity)).thenReturn(eventRoomEntitySaved);
        when(eventRoomConverter.convertEventRoomEntityToResponse(eventRoomEntitySaved)).thenReturn(eventRoomResponse);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventRoomRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(eventRoomResponse)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao salvar uma sala de eventos")
    void testSaveEventRoomError() throws Exception {
        when(eventRoomConverter.convertEventRoomRequestToEntity(eventRoomRequest)).thenReturn(eventRoomEntity);
        when(eventRoomService.saveEventRoom(eventRoomEntity))
                .thenThrow(new EventRoomIllegalArgumentException(MessagesConstants.ROOM_ALREADY_EXISTS, eventRoomEntity.getRoomName()));

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventRoomRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Sala de eventos já existe com o nome: Sala Hard Rock\"}"));
    }

    @Test
    @DisplayName("Deve buscar uma sala de evento pelo id")
    void testFindEventRoomById() throws Exception {
        when(eventRoomService.findEventRoomById(EVENT_ROOM_ID)).thenReturn(eventRoomEntity);
        when(eventRoomConverter.convertEventRoomEntityToResponse(eventRoomEntity)).thenReturn(eventRoomResponse);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/" + EVENT_ROOM_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(eventRoomResponse)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar uma sala de eventos")
    void testFindEventRoomByIdError() throws Exception {
        when(eventRoomService.findEventRoomById(EVENT_ROOM_ID)).thenThrow(new EventRoomNotFoundException(MessagesConstants.ROOM_NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/" + EVENT_ROOM_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Nenhuma sala de eventos encontrada\"}"));
    }

    @Test
    @DisplayName("Deve buscar todas as salas de eventos")
    void testFindAllEventRooms() throws Exception {
        when(eventRoomService.findAllEventRooms()).thenReturn(List.of(eventRoomEntity));
        when(eventRoomConverter.convertEventRoomEntityToResponse(eventRoomEntity)).thenReturn(eventRoomResponse);

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(eventRoomResponse))));
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar todas as salas de eventos")
    void testFindAllEventRoomsError() throws Exception {
        when(eventRoomService.findAllEventRooms()).thenThrow(new EventRoomNotFoundException(MessagesConstants.ROOM_NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Nenhuma sala de eventos encontrada\"}"));
    }

    @Test
    @DisplayName("Deve buscar uma sala de evento pelo nome")
    void testFindByRoomName() throws Exception {
        when(eventRoomService.findByRoomName(eventRoomEntity.getRoomName())).thenReturn(eventRoomEntity);
        when(eventRoomConverter.convertEventRoomEntityToResponse(eventRoomEntity)).thenReturn(eventRoomResponse);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/roomName/" + eventRoomEntity.getRoomName())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(eventRoomResponse)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar uma sala de eventos pelo nome")
    void testFindByRoomNameError() throws Exception {
        when(eventRoomService.findByRoomName(eventRoomEntity.getRoomName())).thenThrow(new EventRoomNotFoundException(MessagesConstants.ROOM_NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/roomName/" + eventRoomEntity.getRoomName())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Nenhuma sala de eventos encontrada\"}"));
    }

    @Test
    @DisplayName("Deve buscar uma sala de evento pelo nome com lock otimista")
    void testFindByRoomNameWithOptimistickLock() throws Exception {
        when(eventRoomService.findByRoomNameWithOptimistickLock(eventRoomEntity.getRoomName())).thenReturn(eventRoomEntity);
        when(eventRoomConverter.convertEventRoomEntityToResponse(eventRoomEntity)).thenReturn(eventRoomResponse);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/roomNameWithOptimistickLock/" + eventRoomEntity.getRoomName())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(eventRoomResponse)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar uma sala de eventos pelo nome com lock otimista")
    void testFindByRoomNameWithOptimistickLockError() throws Exception {
        when(eventRoomService.findByRoomNameWithOptimistickLock(eventRoomEntity.getRoomName())).thenThrow(new EventRoomNotFoundException(MessagesConstants.ROOM_NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/roomNameWithOptimistickLock/" + eventRoomEntity.getRoomName())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Nenhuma sala de eventos encontrada\"}"));
    }

    @Test
    @DisplayName("Deve atualizar uma sala de evento")
    void testUpdateEventRoom() throws Exception {
        EventRoomRequestToUpdate eventRoomRequestToUpdate = EventRoomRequestToUpdate.builder()
                .roomName("Sala Hard Rock")
                .capacity(10)
                .build();

        when(eventRoomConverter.convertEventRoomRequestUpdateToEntity(eventRoomRequestToUpdate)).thenReturn(eventRoomEntity);
        when(eventRoomService.updateEventRoom(eventRoomEntity, EVENT_ROOM_ID)).thenReturn(eventRoomEntitySaved);
        when(eventRoomConverter.convertEventRoomEntityToResponse(eventRoomEntitySaved)).thenReturn(eventRoomResponse);

        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/" + EVENT_ROOM_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventRoomRequestToUpdate)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(eventRoomResponse)));
    }

    @Test
    @DisplayName("Deve retornar um erro ao atualizar uma sala de eventos")
    void testUpdateEventRoomError() throws Exception {
        EventRoomRequestToUpdate eventRoomRequestToUpdate = EventRoomRequestToUpdate.builder()
                .roomName("Sala Hard Rock")
                .capacity(10)
                .build();

        when(eventRoomConverter.convertEventRoomRequestUpdateToEntity(eventRoomRequestToUpdate)).thenReturn(eventRoomEntity);
        when(eventRoomService.updateEventRoom(eventRoomEntity, EVENT_ROOM_ID)).thenThrow(new EventRoomIllegalArgumentException(MessagesConstants.ROOM_NOT_FOUND, EVENT_ROOM_ID));

        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/" + EVENT_ROOM_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventRoomRequestToUpdate)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Nenhuma sala de eventos encontrada\"}"));
    }

    @Test
    @DisplayName("Deve deletar uma sala de evento pelo id")
    void testDeleteEventRoomById() throws Exception {
        when(eventRoomService.findEventRoomById(EVENT_ROOM_ID)).thenReturn(eventRoomEntity);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/" + EVENT_ROOM_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar um erro ao deletar uma sala de eventos pelo id")
    void testDeleteEventRoomByIdError() throws Exception {
        doThrow(new EventRoomNotFoundException(MessagesConstants.ROOM_NOT_FOUND)).when(eventRoomService).deleteEventRoomById(EVENT_ROOM_ID);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/" + EVENT_ROOM_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Nenhuma sala de eventos encontrada\"}"));
    }

}

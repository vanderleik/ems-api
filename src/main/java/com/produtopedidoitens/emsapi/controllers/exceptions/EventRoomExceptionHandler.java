package com.produtopedidoitens.emsapi.controllers.exceptions;

import com.produtopedidoitens.emsapi.application.services.exceptions.eventroom.EventRoomIllegalArgumentException;
import com.produtopedidoitens.emsapi.application.services.exceptions.eventroom.EventRoomNotFoundException;
import com.produtopedidoitens.emsapi.controllers.dtos.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EventRoomExceptionHandler {

    @ExceptionHandler(EventRoomIllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleEventRoomIllegalArgumentException(EventRoomIllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(EventRoomNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEventRoomNotFoundException(EventRoomNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(ex.getMessage()));
    }

}

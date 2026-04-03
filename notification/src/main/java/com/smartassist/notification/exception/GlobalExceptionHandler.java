package com.smartassist.notification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(NotificationNotFoundException ex) {
        // Testin içindeki jsonPath("$.message") ve jsonPath("$.status") ile birebir aynı key'ler
        return new ResponseEntity<>(
            Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.NOT_FOUND.value()
            ), 
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(AlreadyReadException.class)
    public ResponseEntity<Object> handleAlreadyRead(AlreadyReadException ex) {
        return new ResponseEntity<>(
            Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.BAD_REQUEST.value()
            ), 
            HttpStatus.BAD_REQUEST
        );
    }
}
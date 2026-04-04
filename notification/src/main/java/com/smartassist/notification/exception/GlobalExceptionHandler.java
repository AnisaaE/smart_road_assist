package com.smartassist.notification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Validation Hataları (DTO üzerindeki @NotBlank vb. için)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", "Validation Failed"); // Testlerin "message" beklediği için ekledik

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> fieldErrors.put(fe.getField(), fe.getDefaultMessage()));
        body.put("errors", fieldErrors);

        return ResponseEntity.badRequest().body(body);
    }

    // 2. İş Mantığı Hatası (Zaten Okundu durumu için)
    @ExceptionHandler(AlreadyReadException.class)
    public ResponseEntity<Object> handleAlreadyReadException(AlreadyReadException ex) {
        return new ResponseEntity<>(
            Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.BAD_REQUEST.value()
            ), 
            HttpStatus.BAD_REQUEST
        );
    }

    // 3. Bulunamadı Hatası (404 için)
    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotificationNotFoundException ex) {
        return new ResponseEntity<>(
            Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.NOT_FOUND.value()
            ), 
            HttpStatus.NOT_FOUND
        );
    }
}
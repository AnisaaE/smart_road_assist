package com.smartassist.notification.exception;

import com.smartassist.notification.dto.ErrorResponse; // Yeni DTO'muz
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Validation Hataları
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> fieldErrors.put(fe.getField(), fe.getDefaultMessage()));

        // Map yerine ErrorResponse nesnesi dönüyoruz
        ErrorResponse error = ErrorResponse.builder()
                .message("Validation Failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(fieldErrors) // DTO'ya bu alanı eklemeyi unutma
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    // 2. İş Mantığı Hatası (AlreadyReadException)
    @ExceptionHandler(AlreadyReadException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyRead(AlreadyReadException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 3. Bulunamadı Hatası (NotificationNotFoundException)
    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotificationNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // DRY (Don't Repeat Yourself) prensibi için yardımcı metod
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
        ErrorResponse error = ErrorResponse.builder()
                .message(message)
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, status);
    }
}
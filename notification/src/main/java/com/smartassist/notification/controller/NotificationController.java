package com.smartassist.notification.controller;

import com.smartassist.notification.dto.NotificationRequestDTO;
import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.service.INotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor // Constructor injection'ı otomatik yapar
public class NotificationController {

    private final INotificationService notificationService;

    /**
     * Yeni bir bildirim oluşturur.
     * HTTP 201 Created döner.
     */
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(
            @Valid @RequestBody NotificationRequestDTO request) {
        
        NotificationResponseDTO response = notificationService.createNotification(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * ID'ye göre tekil bildirim getirir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable String id) {
        NotificationResponseDTO response = notificationService.getNotificationById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Bildirimi okundu olarak işaretler.
     */
    @PostMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDTO> markAsRead(@PathVariable String id) {
        NotificationResponseDTO response = notificationService.markAsRead(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Belirli bir kullanıcıya ait tüm bildirimleri listeler.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByUserId(@PathVariable String userId) {
        List<NotificationResponseDTO> response = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
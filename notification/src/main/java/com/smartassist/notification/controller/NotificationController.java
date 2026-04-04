package com.smartassist.notification.controller;

import com.smartassist.notification.dto.NotificationRequestDTO;
import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.service.INotificationService;
import jakarta.validation.Valid; // 1. Bu importu ekle
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications") // Başına /api ekledik
public class NotificationController {

    private final INotificationService notificationService;

    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(
            @Valid @RequestBody NotificationRequestDTO request) { // 2. Buraya @Valid ekle
        
        NotificationResponseDTO response = notificationService.createNotification(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable String id) {
        NotificationResponseDTO response = notificationService.getNotificationById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDTO> markAsRead(@PathVariable String id) {
        // Testte "when(...).thenThrow(...)" dediğimiz için 
        // servis katmanı burada hata fırlatacak ve Handler devreye girecek.
        NotificationResponseDTO response = notificationService.markAsRead(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}")
        public ResponseEntity<java.util.List<NotificationResponseDTO>> getNotificationsByUserId(@PathVariable String userId) {
            // Servisi çağırıp listeyi alıyoruz
            java.util.List<NotificationResponseDTO> response = notificationService.getNotificationsByUserId(userId);
            // 200 OK ile listeyi dönüyoruz
            return ResponseEntity.ok(response);
        }
}

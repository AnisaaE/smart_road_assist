package com.smartassist.notification.controller;

import com.smartassist.notification.dto.NotificationRequestDTO;
import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.service.INotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final INotificationService notificationService;

    // Constructor Injection
    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(@RequestBody NotificationRequestDTO request) {
        // Servis katmanını çağırıyoruz
        NotificationResponseDTO response = notificationService.createNotification(request);
        
        // Testin beklediği 201 Created (isCreated) statüsünü dönüyoruz
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    // ... önceki kodlar (constructor ve post metodu)

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable String id) {
        // Servis katmanındaki sağlamlaştırdığımız metodu çağırıyoruz
        NotificationResponseDTO response = notificationService.getNotificationById(id);
        
        // Başarılı ise 200 OK dönüyoruz
        return ResponseEntity.ok(response);
    }
}
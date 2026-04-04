package com.smartassist.notification.controller;

import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.service.INotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationControllerGetByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private INotificationService notificationService;

    @Test
    void whenValidId_thenReturnsNotificationSuccessfully() throws Exception {
        String notificationId = "notif-123";
        
        // 1. Hazırlık (Arrange): Mock bir yanıt oluşturuyoruz
        // DTO'daki constructor yapına göre alanları dolduruyoruz
        NotificationResponseDTO mockResponse = new NotificationResponseDTO(
                notificationId, 
                "user-456", 
                "req-789", 
                "INFO", 
                "Test message for GET", 
                "UNREAD", 
                LocalDateTime.now()
        );

        // 2. Mocklama: Servis bu ID ile çağrıldığında bu yanıtı dönsün
        when(notificationService.getNotificationById(notificationId))
                .thenReturn(mockResponse);

        // 3. Çalıştırma ve Doğrulama (Act & Assert)
        mockMvc.perform(get("/api/notifications/" + notificationId) // URL'ye dikkat: /api/notifications/{id}
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 200 OK Bekliyoruz
                .andExpect(jsonPath("$.id").value(notificationId))
                .andExpect(jsonPath("$.recipientId").value("user-456"))
                .andExpect(jsonPath("$.message").value("Test message for GET"));
    }
}
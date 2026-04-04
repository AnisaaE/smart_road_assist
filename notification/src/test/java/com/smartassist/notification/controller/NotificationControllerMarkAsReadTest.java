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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationControllerMarkAsReadTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private INotificationService notificationService;

    @Test
    void whenMarkAsRead_thenReturnsUpdatedNotification() throws Exception {
        String notificationId = "notif-123";

        // 1. Hazırlık: Durumu "READ" olan mock bir yanıt
        NotificationResponseDTO mockResponse = new NotificationResponseDTO(
                notificationId, "user-123", "req-1", "INFO", "Hello", "READ", LocalDateTime.now());

        // 2. Mocklama
        when(notificationService.markAsRead(notificationId)).thenReturn(mockResponse);

        // 3. Çalıştırma (POST isteği atıyoruz çünkü durum değiştiriyoruz)
        mockMvc.perform(post("/api/notifications/" + notificationId + "/read")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("READ"));
    }
}
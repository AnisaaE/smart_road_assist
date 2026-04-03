package com.smartassist.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartassist.notification.dto.NotificationRequestDTO;
import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.service.INotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class) // Sadece Web (Controller) katmanını ayağa kaldırır
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private INotificationService notificationService; // Servis katmanını mock'luyoruz

    @Autowired
    private ObjectMapper objectMapper; // DTO nesnelerini JSON'a çevirmek için

    private NotificationRequestDTO validRequest;
    private NotificationResponseDTO mockResponse;

    @BeforeEach
    void setUp() {
        // Testlerde kullanılacak örnek veriler
        validRequest = new NotificationRequestDTO(
                "user-123", 
                "req-456", 
                "MECHANIC_ASSIGNED", 
                "Yola çıktık!"
        );

        mockResponse = new NotificationResponseDTO(
                "notif-001", 
                "user-123", 
                "req-456", 
                "MECHANIC_ASSIGNED", 
                "Yola çıktık!", 
                "SENT", 
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("POST /api/notifications - Başarılı bildirim oluşturma (201)")
    void shouldCreateNotificationAndReturn201() throws Exception {
        // GIVEN: Servis metodunun ne döneceğini simüle ediyoruz
        when(notificationService.createNotification(any(NotificationRequestDTO.class)))
                .thenReturn(mockResponse);

        // WHEN & THEN: İsteği at ve sonuçları doğrula
        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated()) // HTTP 201 bekliyoruz
                .andExpect(jsonPath("$.id").value("notif-001"))
                .andExpect(jsonPath("$.status").value("SENT"));
    }

    @Test
    @DisplayName("GET /api/notifications/{id} - ID ile bildirim getirme (200)")
    void shouldReturnNotificationById() throws Exception {
        // GIVEN
        String notifId = "notif-001";
        when(notificationService.getNotificationById(notifId)).thenReturn(mockResponse);

        // WHEN & THEN
        mockMvc.perform(get("/api/notifications/" + notifId))
                .andExpect(status().isOk()) // HTTP 200 bekliyoruz
                .andExpect(jsonPath("$.id").value(notifId))
                .andExpect(jsonPath("$.recipientId").value("user-123"))
                .andExpect(jsonPath("$.status").value("SENT"));
    }
}
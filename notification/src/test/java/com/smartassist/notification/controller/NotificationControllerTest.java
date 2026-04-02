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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class) // Sadece Web katmanını ayağa kaldırır
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private INotificationService notificationService; // Servisi mock'luyoruz

    @Autowired
    private ObjectMapper objectMapper; // DTO'yu JSON'a çevirmek için

    private NotificationRequestDTO validRequest;
    private NotificationResponseDTO mockResponse;

    @BeforeEach
    void setUp() {
        validRequest = new NotificationRequestDTO(
                "user-123", "req-456", "MECHANIC_ASSIGNED", "Yola çıktık!"
        );

        mockResponse = new NotificationResponseDTO(
                "notif-001", "user-123", "req-456", 
                "MECHANIC_ASSIGNED", "Yola çıktık!", "SENT", LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("POST /notifications - Başarılı bir şekilde bildirim oluşturmalı")
    void shouldCreateNotificationAndReturn201() throws Exception {
        // GIVEN
        when(notificationService.createNotification(any(NotificationRequestDTO.class)))
                .thenReturn(mockResponse);

        // WHEN & THEN
        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated()) // 201 Created bekliyoruz
                .andExpect(jsonPath("$.id").value("notif-001"))
                .andExpect(jsonPath("$.status").value("SENT"));
    }
}
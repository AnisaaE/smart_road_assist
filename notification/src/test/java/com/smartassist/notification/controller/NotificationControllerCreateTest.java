package com.smartassist.notification.controller;

import com.smartassist.notification.dto.NotificationRequestDTO;
import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.service.INotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationControllerCreateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private INotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper; // DTO'yu JSON'a çevirmek için

    @Test
    void shouldCreateNotificationSuccessfully() throws Exception {
        // 1. Hazırlık (Arrange)
        NotificationRequestDTO request = new NotificationRequestDTO(
                "user-123", "req-001", "INFO", "Hello TDD!");

        NotificationResponseDTO response = new NotificationResponseDTO();
        response.setRecipientId("user-123");
        response.setMessage("Hello TDD!");
        // ID ve timestamp gibi alanlar normalde serviste setlenir, mockluyoruz:
        // response.setId("notif-abc"); 

        // 2. Mocklama
        when(notificationService.createNotification(any(NotificationRequestDTO.class)))
                .thenReturn(response);

        // 3. Çalıştırma ve Doğrulama (Act & Assert)
        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // 201 Created
                .andExpect(jsonPath("$.recipientId").value("user-123"))
                .andExpect(jsonPath("$.message").value("Hello TDD!"));
    }
}
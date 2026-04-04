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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationControllerGetUserNotificationsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private INotificationService notificationService;

    @Test
    void whenUserHasNotifications_thenReturnsListSuccessfully() throws Exception {
        String userId = "user-123";

        // 1. Hazırlık: Mock liste (2 adet bildirim içeriyor)
        NotificationResponseDTO n1 = new NotificationResponseDTO(
                "n-1", userId, "req-1", "INFO", "Message 1", "UNREAD", LocalDateTime.now());
        NotificationResponseDTO n2 = new NotificationResponseDTO(
                "n-2", userId, "req-2", "ALERT", "Message 2", "READ", LocalDateTime.now());

        List<NotificationResponseDTO> mockList = List.of(n1, n2);

        // 2. Mocklama
        when(notificationService.getNotificationsByUserId(userId)).thenReturn(mockList);

        // 3. Çalıştırma
        mockMvc.perform(get("/api/notifications/user/" + userId) // Endpoint yapısına dikkat
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2)) // Listenin uzunluğu 2 mi?
                .andExpect(jsonPath("$[0].id").value("n-1"))
                .andExpect(jsonPath("$[1].id").value("n-2"))
                .andExpect(jsonPath("$[0].recipientId").value(userId));
    }
}
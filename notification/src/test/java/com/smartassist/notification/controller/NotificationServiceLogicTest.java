package com.smartassist.notification.controller;

import com.smartassist.notification.exception.AlreadyReadException;
import com.smartassist.notification.service.INotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// Statik Importlar (Bunlar manuel eklenmelidir veya IDE'ye "static import" olarak yaptırılmalıdır)
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationServiceLogicTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private INotificationService notificationService;

    @Test
    void whenNotificationAlreadyRead_thenReturns400() throws Exception {
        String notificationId = "notif-123";

        // Mock: Servis katmanı çağrıldığında AlreadyReadException fırlatır
        when(notificationService.markAsRead(notificationId))
                .thenThrow(new AlreadyReadException("Notification is already marked as read"));

        mockMvc.perform(post("/api/notifications/" + notificationId + "/read")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // 400 Bekliyoruz
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Notification is already marked as read"));
    }
}
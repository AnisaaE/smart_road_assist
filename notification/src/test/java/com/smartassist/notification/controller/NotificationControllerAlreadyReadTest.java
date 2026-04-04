package com.smartassist.notification.controller;

import com.smartassist.notification.exception.AlreadyReadException;
import com.smartassist.notification.exception.GlobalExceptionHandler;
import com.smartassist.notification.service.INotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@Import(GlobalExceptionHandler.class)
public class NotificationControllerAlreadyReadTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private INotificationService notificationService;

    @Test
    void whenNotificationAlreadyRead_thenReturns400() throws Exception {
        String notificationId = "notif-123";

        // 1. Mocklama: Servis bu ID ile çağrıldığında AlreadyReadException fırlatsın
        when(notificationService.markAsRead(notificationId))
                .thenThrow(new AlreadyReadException("Notification is already read!"));

        // 2. Çalıştırma ve Doğrulama
        mockMvc.perform(post("/api/notifications/" + notificationId + "/read")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // 400 Bad Request
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Notification is already read!"));
    }
}
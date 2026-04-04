package com.smartassist.notification.controller;

import com.smartassist.notification.exception.GlobalExceptionHandler;
import com.smartassist.notification.service.INotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@Import(GlobalExceptionHandler.class) // Bunu ekle
public class NotificationValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private INotificationService notificationService;

    @Test
    void whenMessageIsEmpty_thenReturns400() throws Exception {
        String invalidJson = "{\"recipientId\": \"user-123\", \"type\": \"SMS\", \"message\": \"\"}";

        // BURAYI DEĞİŞTİR: Başına /api ekle
        mockMvc.perform(post("/api/notifications") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}

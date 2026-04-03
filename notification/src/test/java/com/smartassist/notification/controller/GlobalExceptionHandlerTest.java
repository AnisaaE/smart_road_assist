package com.smartassist.notification.controller;

import com.smartassist.notification.exception.NotificationNotFoundException;
import com.smartassist.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    void whenNotificationNotFound_thenReturns404AndCustomMessage() throws Exception {
        Long invalidId = 999L;
        
        // Mock: Servis katmanı bu ID ile çağrıldığında custom exception fırlatsın
        when(notificationService.getNotificationById(invalidId))
                .thenThrow(new NotificationNotFoundException("Notification not found with id: " + invalidId));

        mockMvc.perform(get("/api/notifications/" + invalidId)
                .contentType(MediaType.APPLICATION_BITSTREAM)) // veya APPLICATION_JSON
                .andExpect(status().isNotFound()) // 404 Bekliyoruz
                .andExpect(jsonPath("$.message").value("Notification not found with id: 999"))
                .andExpect(jsonPath("$.status").value(404));
    }
}
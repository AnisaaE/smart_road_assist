package com.smartassist.notification.controller;

import com.smartassist.notification.exception.NotificationNotFoundException;
import com.smartassist.notification.service.INotificationService; // Interface kullandığından emin ol
import com.smartassist.notification.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationController.class)
@Import(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private INotificationService notificationService; // Interface'i mock'lamak daha garanti

    @Test
    void whenNotificationNotFound_thenReturns404AndCustomMessage() throws Exception {
        String invalidId = "999"; 
        
        // GIVEN: Servis bu hatayı fırlattığında
        when(notificationService.getNotificationById(invalidId))
                .thenThrow(new NotificationNotFoundException("Notification not found with id: " + invalidId));

        // WHEN & THEN
        // DİKKAT: "/api/notifications" olarak güncellendi!
        mockMvc.perform(get("/api/notifications/" + invalidId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Notification not found with id: 999"))
                .andExpect(jsonPath("$.status").value(404));
    }
}
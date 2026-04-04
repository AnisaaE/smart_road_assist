package com.smartassist.notification.controller;

import com.smartassist.notification.exception.GlobalExceptionHandler;
import com.smartassist.notification.exception.NotificationNotFoundException;
import com.smartassist.notification.service.INotificationService;
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

@WebMvcTest(NotificationController.class)
@Import(GlobalExceptionHandler.class) // Hata yakalayıcıyı dahil ettik
public class NotificationControllerNotFoundTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private INotificationService notificationService;

    @Test
    void whenNotificationIdDoesNotExist_thenReturns404() throws Exception {
        String nonExistentId = "notif-999";

        // 1. Mocklama: Servis bu ID ile çağrıldığında bizim özel exception'ımızı fırlatsın
        when(notificationService.getNotificationById(nonExistentId))
                .thenThrow(new NotificationNotFoundException("Notification not found with id: " + nonExistentId));

        // 2. Çalıştırma ve Doğrulama
        mockMvc.perform(get("/api/notifications/" + nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // 404 Bekliyoruz
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Notification not found with id: " + nonExistentId));
    }
}
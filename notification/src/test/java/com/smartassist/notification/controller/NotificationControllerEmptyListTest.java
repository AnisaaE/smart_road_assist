package com.smartassist.notification.controller;

import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.service.INotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationControllerEmptyListTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private INotificationService notificationService;

    @Test
    void whenUserHasNoNotifications_thenReturnsEmptyList() throws Exception {
        String userId = "new-user-999";

        // 1. Hazırlık: Boş bir liste döndüreceğimizi söylüyoruz
        List<NotificationResponseDTO> emptyList = Collections.emptyList();

        // 2. Mocklama
        when(notificationService.getNotificationsByUserId(userId)).thenReturn(emptyList);

        // 3. Çalıştırma
        mockMvc.perform(get("/api/notifications/user/" + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0)) // Listenin uzunluğu 0 olmalı
                .andExpect(jsonPath("$").isArray()); // Dönen şey bir dizi mi?
    }
}
package com.smartassist.notification.service;

import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.model.Notification;
import com.smartassist.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional; // Optional için şart

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*; // when ve verify buradan geliyor
@ExtendWith(MockitoExtension.class)
public class NotificationServiceUpdateStatusTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void whenMarkAsRead_shouldChangeStatusToRead() {
        // GIVEN
        String id = "notif-1";
        Notification existingNotification = new Notification();
        existingNotification.setId(id);
        existingNotification.setStatus("UNREAD");

        // Mocklama
        when(notificationRepository.findById(id)).thenReturn(Optional.of(existingNotification));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(i -> i.getArguments()[0]);

        // WHEN
        NotificationResponseDTO response = notificationService.markAsRead(id);

        // THEN
        assertEquals("READ", response.getStatus());
        verify(notificationRepository).save(any(Notification.class));
    }
}
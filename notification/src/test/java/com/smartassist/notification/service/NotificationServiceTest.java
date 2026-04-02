package com.smartassist.notification.service;

import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.model.Notification;
import com.smartassist.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * TDD — RED PHASE
 *
 * NotificationService için basit unit test.
 * Mevcut sınıflar: Notification, NotificationResponseDTO, NotificationRepository
 *
 * Bu test şu an RED'dir çünkü NotificationService henüz yok.
 * GREEN için NotificationService sınıfı oluşturulmalıdır.
 */
@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldReturnNotificationWhenIdExists() {
        // GIVEN
        Notification notification = new Notification();
        notification.setId("notif-001");
        notification.setRecipientId("user-001");
        notification.setRequestId("req-001");
        notification.setType("MECHANIC_ASSIGNED");
        notification.setMessage("Mechanic assigned to your request");
        notification.setStatus("SENT");
        notification.setSentAt(LocalDateTime.of(2025, 1, 1, 10, 0));

        when(notificationRepository.findById("notif-001"))
                .thenReturn(Optional.of(notification));

        // WHEN
        NotificationResponseDTO result = notificationService.getNotificationById("notif-001");

        // THEN
        assertNotNull(result);
        assertEquals("notif-001", result.getId());
        assertEquals("SENT", result.getStatus());
        assertEquals("user-001", result.getRecipientId());
    }
}
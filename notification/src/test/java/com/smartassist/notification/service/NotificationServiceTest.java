package com.smartassist.notification.service;

import com.smartassist.notification.exception.AlreadyReadException;
import com.smartassist.notification.exception.NotificationNotFoundException;
import com.smartassist.notification.model.Notification;
import com.smartassist.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TDD — RED → GREEN
 */
@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification sentNotification;
    private Notification readNotification;

    @BeforeEach
    void setUp() {
        // Test verisi: Gönderilmiş (SENT) bildirim
        sentNotification = new Notification();
        sentNotification.setId("notif-001");
        sentNotification.setRecipientId("user-001");
        sentNotification.setRequestId("req-001");
        sentNotification.setType("MECHANIC_ASSIGNED");
        sentNotification.setMessage("Mechanic assigned");
        sentNotification.setStatus("SENT");
        sentNotification.setSentAt(LocalDateTime.of(2025, 1, 1, 10, 0));

        // Test verisi: Okunmuş (READ) bildirim
        readNotification = new Notification();
        readNotification.setId("notif-002");
        readNotification.setRecipientId("user-001");
        readNotification.setRequestId("req-001");
        readNotification.setType("REQUEST_COMPLETED");
        readNotification.setMessage("Request completed");
        readNotification.setStatus("READ");
        readNotification.setSentAt(LocalDateTime.of(2025, 1, 1, 11, 0));
    }

    // ─── getNotificationById Testleri ──────────────────────────────────────────

    @Test
    @DisplayName("ID mevcut olduğunda bildirimi başarıyla dönmeli")
    void shouldReturnNotificationWhenIdExists() {
        // GIVEN
        when(notificationRepository.findById("notif-001"))
                .thenReturn(Optional.of(sentNotification));

        // WHEN
        var result = notificationService.getNotificationById("notif-001");

        // THEN
        assertNotNull(result);
        assertEquals("notif-001", result.getId());
        assertEquals("SENT", result.getStatus());
        assertEquals("user-001", result.getRecipientId());
    }

    @Test
    @DisplayName("Olmayan ID için NotFoundException fırlatmalı")
    void getNotification_ShouldThrowNotFoundException() {
        // GIVEN
        when(notificationRepository.findById("not-exist"))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        NotificationNotFoundException ex = assertThrows(
                NotificationNotFoundException.class,
                () -> notificationService.getNotificationById("not-exist")
        );
        assertTrue(ex.getMessage().contains("not-exist"));
    }

    // ─── markAsRead Testleri ───────────────────────────────────────────────────

    @Test
    @DisplayName("SENT durumundaki bildirimi READ olarak güncellemeli")
    void markAsRead_ShouldUpdateStatus() {
        // GIVEN
        when(notificationRepository.findById("notif-001"))
                .thenReturn(Optional.of(sentNotification));

        Notification saved = new Notification();
        saved.setId("notif-001");
        saved.setStatus("READ"); // Güncellenmiş hali

        when(notificationRepository.save(any(Notification.class))).thenReturn(saved);

        // WHEN
        var result = notificationService.markAsRead("notif-001");

        // THEN
        assertEquals("READ", result.getStatus());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
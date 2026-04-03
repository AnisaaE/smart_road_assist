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
 * Commit sırası:
 *   commit 1: [RED]   NotificationServiceTest dosyası eklendi
 *   commit 2: [GREEN] NotificationService.getNotificationById eklendi
 *   commit 3: [GREEN] NotificationService.markAsRead eklendi
 *   commit 4: [REFACTOR] toResponse helper metodu ayrıştırıldı
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
        sentNotification = new Notification();
        sentNotification.setId("notif-001");
        sentNotification.setRecipientId("user-001");
        sentNotification.setRequestId("req-001");
        sentNotification.setType("MECHANIC_ASSIGNED");
        sentNotification.setMessage("Mechanic assigned");
        sentNotification.setStatus("SENT");
        sentNotification.setSentAt(LocalDateTime.of(2025, 1, 1, 10, 0));

        readNotification = new Notification();
        readNotification.setId("notif-002");
        readNotification.setRecipientId("user-001");
        readNotification.setRequestId("req-001");
        readNotification.setType("REQUEST_COMPLETED");
        readNotification.setMessage("Request completed");
        readNotification.setStatus("READ");
        readNotification.setSentAt(LocalDateTime.of(2025, 1, 1, 11, 0));
    }

    // ─── getNotificationById ──────────────────────────────────────────────────

    @Test
    @DisplayName("shouldReturnNotificationWhenIdExists")
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

    // ─── getNotification_ShouldThrowNotFoundException ─────────────────────────

    @Test
    @DisplayName("getNotification_ShouldThrowNotFoundException")
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

    // ─── markAsRead_ShouldUpdateStatus ────────────────────────────────────────

    @Test
    @DisplayName("markAsRead_ShouldUpdateStatus")
    void markAsRead_ShouldUpdateStatus() {
        // GIVEN
        when(notificationRepository.findById("notif-001"))
                .thenReturn(Optional.of(sentNotification));

        Notification saved = new Notification();
        saved.setId("notif-001");
        saved.setRecipientId("user-001");
        saved.setRequestId("req-001");
        saved.setType("MECHANIC_ASSIGNED");
        saved.setMessage("Mechanic assigned");
        saved.setStatus("READ");
        saved.setSentAt(sentNotification.getSentAt());

        when(notificationRepository.save(any(Notification.class))).thenReturn(saved);

        // WHEN
        var result = notificationService.markAsRead("notif-001");

        // THEN
        assertEquals("READ", result.getStatus());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    // ─── markAsRead_ShouldThrowAlreadyReadException ───────────────────────────

    @Test
    @DisplayName("markAsRead_ShouldThrowAlreadyReadException")
    void markAsRead_ShouldThrowAlreadyReadException() {
        // GIVEN: bildirim zaten READ durumunda
        when(notificationRepository.findById("notif-002"))
                .thenReturn(Optional.of(readNotification));

        // WHEN & THEN
        AlreadyReadException ex = assertThrows(
                AlreadyReadException.class,
                () -> notificationService.markAsRead("notif-002")
        );
        assertTrue(ex.getMessage().contains("already READ"));
    }

    // ─── markAsRead olmayan id ────────────────────────────────────────────────

    @Test
    @DisplayName("markAsRead_ShouldThrowNotFoundException_WhenIdNotExist")
    void markAsRead_ShouldThrowNotFoundException_WhenIdNotExist() {
        // GIVEN
        when(notificationRepository.findById("not-exist"))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                NotificationNotFoundException.class,
                () -> notificationService.markAsRead("not-exist")
        );
    }
}
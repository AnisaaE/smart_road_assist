package com.smartassist.notification.service;

import com.smartassist.notification.dto.NotificationRequestDTO;
import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.model.Notification;
import com.smartassist.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private NotificationRequestDTO validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new NotificationRequestDTO(
                "user-123",
                "req-456",
                "MECHANIC_ASSIGNED",
                "Aracınız için bir teknisyen atandı."
        );
    }

    @Test
    @DisplayName("TDD 1: Bildirim başarıyla kaydedilmeli ve SENT statüsünde dönmeli")
    void shouldCreateNotificationSuccessfully() {
        // GIVEN
        Notification savedNotification = new Notification();
        savedNotification.setId("notif-001");
        savedNotification.setRecipientId(validRequest.getRecipientId());
        savedNotification.setStatus("SENT"); // İş mantığımızın beklentisi

        // Repository save edildiğinde bu objeyi dönecek şekilde mock'luyoruz
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        // WHEN
        NotificationResponseDTO response = notificationService.createNotification(validRequest);

        // THEN
        assertNotNull(response.getId());
        assertEquals("SENT", response.getStatus());
        assertEquals(validRequest.getRecipientId(), response.getRecipientId());
        
        // Veritabanına kaydetme işleminin 1 kere yapıldığını doğrula
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
}
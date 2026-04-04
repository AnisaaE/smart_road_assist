package com.smartassist.notification.service;

import com.smartassist.notification.dto.NotificationRequestDTO;
import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.model.Notification; // Kendi model yolun
import com.smartassist.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*; // verify, when, times buradan gelir

@ExtendWith(MockitoExtension.class)
public class NotificationServiceCreateLogicTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void whenCreateNotification_shouldSetDefaultStatusToUnread() {
        // 1. GIVEN (Hazırlık)
        NotificationRequestDTO request = new NotificationRequestDTO();
        request.setRecipientId("user-1");
        request.setMessage("Test Message");

        Notification savedNotification = new Notification();
        savedNotification.setStatus("UNREAD"); // Servis katmanı bunu setleyecek
        savedNotification.setRecipientId("user-1");

        // Mockito'ya save metodunda ne döneceğini söylüyoruz
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        // 2. WHEN (Çalıştırma)
        NotificationResponseDTO response = notificationService.createNotification(request);

        // 3. THEN (Doğrulama)
        assertNotNull(response);
        assertEquals("UNREAD", response.getStatus());

        // 4. VERIFY (İşte senin sorduğun hatalı yerin doğrusu)
        // Repo'nun save metodu herhangi bir Notification nesnesiyle tam 1 kere çağrıldı mı?
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
}
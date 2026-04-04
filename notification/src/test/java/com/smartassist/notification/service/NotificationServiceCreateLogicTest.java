package com.smartassist.notification.service;

import com.smartassist.notification.dto.NotificationRequestDTO;
import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.repository.NotificationRepository; // Henüz yazmadıysan hata verebilir
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceCreateLogicTest {

    @Mock
    private NotificationRepository notificationRepository; // Repo katmanını mockluyoruz

    @InjectMocks
    private NotificationServiceImpl notificationService; // Gerçek servis sınıfın

    private NotificationRequestDTO validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new NotificationRequestDTO(
                "user-1", "req-1", "INFO", "Logic Test Message");
    }

    @Test
    void whenCreateNotification_shouldSetDefaultStatusToUnread() {
        // Act: Servisi çalıştır
        NotificationResponseDTO response = notificationService.createNotification(validRequest);

        // Assert: Mantık kontrolü
        assertNotNull(response);
        assertEquals("UNREAD", response.getStatus()); // Servis bunu otomatik setlemeli!
        assertEquals("Logic Test Message", response.getMessage());
        
        // Repository'nin save metodunun çağrıldığını doğrula
        // verify(notificationRepository).save(any()); 
    }
}
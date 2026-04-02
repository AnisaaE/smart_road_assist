package com.smartassist.notification.repository;

import com.smartassist.notification.model.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongodb.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest // Sadece MongoDB bileşenlerini ayağa kaldırır
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("shouldFindByRecipientId - Kullanıcıya ait bildirimleri getirmeli")
    void shouldFindByRecipientId() {
        // GIVEN
        Notification n1 = new Notification();
        n1.setRecipientId("user-999");
        n1.setStatus("SENT");
        notificationRepository.save(n1);

        // WHEN
        List<Notification> results = notificationRepository.findByRecipientId("user-999");

        // THEN
        assertFalse(results.isEmpty());
        assertEquals("user-999", results.get(0).getRecipientId());
    }
}
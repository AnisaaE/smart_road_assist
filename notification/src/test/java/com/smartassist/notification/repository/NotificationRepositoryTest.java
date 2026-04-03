package com.smartassist.notification.repository;

import com.smartassist.notification.model.Notification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @AfterEach
    void tearDown() {
        notificationRepository.deleteAll(); // Her testten sonra DB'yi temizler
    }

    @Test
    @DisplayName("findByRecipientId - Verilen kullanıcıya ait tüm bildirimleri dönmeli")
    void findByUserId_ShouldReturnList() {
        // GIVEN
        Notification n1 = new Notification();
        n1.setRecipientId("user-001");
        n1.setStatus("SENT");
        notificationRepository.save(n1);

        Notification n2 = new Notification();
        n2.setRecipientId("user-001");
        n2.setStatus("READ");
        notificationRepository.save(n2);

        // WHEN
        List<Notification> results = notificationRepository.findByRecipientId("user-001");

        // THEN
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("findByRecipientId - Başka kullanıcıların bildirimlerini getirmemeli")
    void findByRecipientId_ShouldNotReturnOtherUsersNotifications() {
        // GIVEN
        Notification n1 = new Notification();
        n1.setRecipientId("user-001");
        notificationRepository.save(n1);

        Notification n2 = new Notification();
        n2.setRecipientId("user-002");
        notificationRepository.save(n2);

        // WHEN
        List<Notification> results = notificationRepository.findByRecipientId("user-001");

        // THEN
        assertEquals(1, results.size());
        assertEquals("user-001", results.get(0).getRecipientId());
    }
}
package com.smartassist.notification.repository;

import com.smartassist.notification.NotificationApplication; 
import com.smartassist.notification.model.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongodb.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = NotificationApplication.class)
@DataMongoTest // Sadece MongoDB bileşenlerini (ve gömülü Mongo'yu) ayağa kaldırır
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("shouldFindByRecipientId - Kullanıcıya ait bildirimleri getirmeli")
    void shouldFindByRecipientId() {
        // GIVEN: Veritabanına test verisi ekliyoruz
        Notification n1 = new Notification();
        n1.setRecipientId("user-999");
        n1.setStatus("SENT");
        notificationRepository.save(n1);

        // WHEN: Özel sorgu metodumuzu çağırıyoruz
        List<Notification> results = notificationRepository.findByRecipientId("user-999");

        // THEN: Sonuçların doğruluğunu kontrol ediyoruz
        assertNotNull(results, "Sonuç listesi null olmamalı");
        assertFalse(results.isEmpty(), "Liste boş olmamalı");
        assertEquals("user-999", results.get(0).getRecipientId(), "Dönen bildirim doğru kullanıcıya ait olmalı");
    }
}
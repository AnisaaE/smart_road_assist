package com.smartassist.notification.repository;

import com.smartassist.notification.NotificationApplication; // Ana uygulama sınıfın
import com.smartassist.notification.model.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith; // Eksik olan buydu
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongodb.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension; // Eksik olan buydu

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) 
@ContextConfiguration(classes = NotificationApplication.class) 
@DataMongoTest 
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
        assertNotNull(results, "Sonuç listesi null olmamalı");
        assertFalse(results.isEmpty(), "Liste boş olmamalı");
        assertEquals("user-999", results.get(0).getRecipientId());
    }
}
package com.smartassist.notification.repository;

import com.smartassist.notification.model.Notification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongodb.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @AfterEach
    void tearDown() {
        notificationRepository.deleteAll();
    }

    @Test
    @DisplayName("findByUserId_ShouldReturnList")
    void findByUserId_ShouldReturnList() {
        // GIVEN
        Notification n1 = new Notification();
        n1.setRecipientId("user-001");
        n1.setRequestId("req-001");
        n1.setType("MECHANIC_ASSIGNED");
        n1.setMessage("Mechanic assigned");
        n1.setStatus("SENT");
        notificationRepository.save(n1);

        Notification n2 = new Notification();
        n2.setRecipientId("user-001");
        n2.setRequestId("req-002");
        n2.setType("REQUEST_COMPLETED");
        n2.setMessage("Request completed");
        n2.setStatus("READ");
        notificationRepository.save(n2);

        // WHEN
        List<Notification> results = notificationRepository.findByRecipientId("user-001");

        // THEN
        assertEquals(2, results.size());
        results.forEach(r -> assertEquals("user-001", r.getRecipientId()));
    }

    @Test
    @DisplayName("findByRecipientId_ShouldNotReturnOtherUsersNotifications")
    void findByRecipientId_ShouldNotReturnOtherUsersNotifications() {
        // GIVEN
        Notification n1 = new Notification();
        n1.setRecipientId("user-001");
        n1.setType("MECHANIC_ASSIGNED");
        n1.setMessage("msg");
        n1.setStatus("SENT");
        notificationRepository.save(n1);

        Notification n2 = new Notification();
        n2.setRecipientId("user-002");
        n2.setType("PAYMENT_RECEIVED");
        n2.setMessage("msg");
        n2.setStatus("SENT");
        notificationRepository.save(n2);

        // WHEN
        List<Notification> results = notificationRepository.findByRecipientId("user-001");

        // THEN
        assertEquals(1, results.size());
        assertEquals("user-001", results.get(0).getRecipientId());
    }
    @Test
        @DisplayName("markAsRead_ShouldThrowNotFoundException_WhenIdNotExist")
        void markAsRead_ShouldThrowNotFoundException_WhenIdNotExist() {
            when(notificationRepository.findById("not-exist"))
                    .thenReturn(Optional.empty());

            assertThrows(
                    NotificationNotFoundException.class,
                    () -> notificationService.markAsRead("not-exist")
            );
        }
    }         
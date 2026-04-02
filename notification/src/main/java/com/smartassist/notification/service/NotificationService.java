package com.smartassist.notification.service;

import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.model.Notification;
import com.smartassist.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationResponseDTO getNotificationById(String id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + id));
        return toResponse(n);
    }

    public NotificationResponseDTO createNotification(String recipientId, String requestId,
                                                       String type, String message) {
        Notification notification = new Notification();
        notification.setRecipientId(recipientId);
        notification.setRequestId(requestId);
        notification.setType(type);
        notification.setMessage(message);
        notification.setStatus("SENT");
        notification.setSentAt(LocalDateTime.now());

        return toResponse(notificationRepository.save(notification));
    }

    public List<NotificationResponseDTO> getNotificationsByRecipientId(String recipientId) {
        return notificationRepository.findByRecipientId(recipientId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private NotificationResponseDTO toResponse(Notification n) {
        return new NotificationResponseDTO(
                n.getId(),
                n.getRecipientId(),
                n.getRequestId(),
                n.getType(),
                n.getMessage(),
                n.getStatus(),
                n.getSentAt()
        );
    }
}
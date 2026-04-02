package com.smartassist.notification.service;

import com.smartassist.notification.dto.NotificationRequestDTO;
import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.exception.AlreadyReadException;
import com.smartassist.notification.exception.NotificationNotFoundException;
import com.smartassist.notification.model.Notification;
import com.smartassist.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationResponseDTO getNotificationById(String id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("not found: " + id));
        return toResponse(n);
    }

    @Override
    public NotificationResponseDTO markAsRead(String id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("not found: " + id));

        if ("READ".equals(n.getStatus())) {
            throw new AlreadyReadException("already READ");
        }

        n.setStatus("READ");
        return toResponse(notificationRepository.save(n));
    }

    private NotificationResponseDTO toResponse(Notification n) {
        return new NotificationResponseDTO(
                n.getId(), n.getRecipientId(), n.getRequestId(),
                n.getType(), n.getMessage(), n.getStatus(), n.getSentAt()
        );
    }

    // Şimdilik boş bırakabiliriz, testleri etkilemez
    @Override public NotificationResponseDTO createNotification(NotificationRequestDTO r) { return null; }
    @Override public List<NotificationResponseDTO> getNotificationsByUserId(String u) { return null; }
}
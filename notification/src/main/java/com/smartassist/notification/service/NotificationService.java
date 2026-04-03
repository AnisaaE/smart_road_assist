package com.smartassist.notification.service;

import com.smartassist.notification.dto.NotificationRequestDTO;
import com.smartassist.notification.dto.NotificationResponseDTO;
import com.smartassist.notification.exception.AlreadyReadException;
import com.smartassist.notification.exception.NotificationNotFoundException;
import com.smartassist.notification.model.Notification;
import com.smartassist.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO request) {
        Notification notification = new Notification();
        notification.setRecipientId(request.getRecipientId());
        notification.setRequestId(request.getRequestId());
        notification.setType(request.getType());
        notification.setMessage(request.getMessage());
        notification.setStatus("SENT");
        notification.setSentAt(LocalDateTime.now());

        return toResponse(notificationRepository.save(notification));
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByUserId(String userId) {
        return notificationRepository.findByRecipientId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationResponseDTO getNotificationById(String id) {
        return notificationRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotificationNotFoundException("not found: " + id));
    }

    @Override
    public NotificationResponseDTO markAsRead(String id) {
        return notificationRepository.findById(id)
                .map(n -> {
                    validateNotAlreadyRead(n);
                    n.setStatus("READ");
                    return notificationRepository.save(n); // Güncellenen objeyi kaydet
                })
                .map(this::toResponse) // Kaydedilen objeyi DTO'ya çevir
                .orElseThrow(() -> new NotificationNotFoundException("not found: " + id));
    }

    private void validateNotAlreadyRead(Notification n) {
        if ("READ".equals(n.getStatus())) {
            throw new AlreadyReadException("already READ");
        }
    }

    private NotificationResponseDTO toResponse(Notification n) {
        return new NotificationResponseDTO(
                n.getId(), n.getRecipientId(), n.getRequestId(),
                n.getType(), n.getMessage(), n.getStatus(), n.getSentAt()
        );
    }
}
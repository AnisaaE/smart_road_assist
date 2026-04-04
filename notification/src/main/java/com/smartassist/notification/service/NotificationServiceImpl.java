package com.smartassist.notification.service;

import com.smartassist.notification.dto.*;
import com.smartassist.notification.model.Notification; // Entity/Model yoluna dikkat et
import com.smartassist.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements INotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO request) {
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setRecipientId(request.getRecipientId());
        notification.setMessage(request.getMessage());
        notification.setStatus("UNREAD");
        
        // HATALI: notification.setCreatedAt(LocalDateTime.now()); 
        // DOĞRU: Modeldeki isme (sentAt) göre setliyoruz
        notification.setSentAt(LocalDateTime.now()); 

        Notification savedNotification = notificationRepository.save(notification);
        return convertToResponseDTO(savedNotification);
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByUserId(String userId) {
        // Repo'daki findByRecipientId metodunu kullanıyoruz
        return notificationRepository.findByRecipientId(userId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationResponseDTO getNotificationById(String id) {
        // FindById kontrolü
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found")); // Custom exception'a çevrilebilir
        return convertToResponseDTO(notification);
    }

    @Override
    public NotificationResponseDTO markAsRead(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        notification.setStatus("READ");
        return convertToResponseDTO(notificationRepository.save(notification));
    }

    private NotificationResponseDTO convertToResponseDTO(Notification n) {
        return new NotificationResponseDTO(
            n.getId(), 
            n.getRecipientId(), 
            n.getRequestId(), // "req-default" yerine n.getRequestId() kullanabilirsin, modelde var
            n.getType(),      // "INFO" yerine n.getType() kullanabilirsin, modelde var
            n.getMessage(), 
            n.getStatus(), 
            n.getSentAt()     // İŞTE BURASI: getCreatedAt() yerine getSentAt() yazıyoruz
        );
    };
 }

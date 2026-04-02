package com.smartassist.notification.service;
import com.smartassist.notification.dto.*;
import java.util.List;

public interface INotificationService {
    NotificationResponseDTO getNotificationById(String id);
    NotificationResponseDTO markAsRead(String id);
    NotificationResponseDTO createNotification(NotificationRequestDTO request);
    List<NotificationResponseDTO> getNotificationsByUserId(String userId);
}
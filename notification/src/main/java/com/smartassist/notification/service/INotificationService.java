package com.smartassist.notification.service;

import com.smartassist.notification.dto.*;
import java.util.List; // 1. BU IMPORT ŞART!

public interface INotificationService {
    NotificationResponseDTO getNotificationById(String id);
    NotificationResponseDTO markAsRead(String id);
    NotificationResponseDTO createNotification(NotificationRequestDTO request);
    
    // Testin aradığı metod burada:
    List<NotificationResponseDTO> getNotificationsByUserId(String userId); 
}
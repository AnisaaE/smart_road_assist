package com.smartassist.notification.dto;

import java.time.LocalDateTime;

public class NotificationResponseDTO {
    private String id;
    private String recipientId;
    private String requestId;
    private String type;
    private String message;
    private String status;
    private LocalDateTime sentAt;

    // Tüm alanları içeren Constructor (Servis için kritik)
    public NotificationResponseDTO(String id, String recipientId, String requestId, 
                                   String type, String message, String status, LocalDateTime sentAt) {
        this.id = id;
        this.recipientId = recipientId;
        this.requestId = requestId;
        this.type = type;
        this.message = message;
        this.status = status;
        this.sentAt = sentAt;
    }

    // Getters (Testlerin doğrulanması için gerekli)
    public String getId() { return id; }
    public String getRecipientId() { return recipientId; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    // Diğer getter'ları ihtiyaca göre ekleyebilirsin
}
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

    // Servis içindeki return new NotificationResponseDTO(...) çağrısı için gereken Constructor
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

    // Test içindeki assertEquals(..., response.getStatus()) için gereken Getterlar
    public String getId() { return id; }
    public String getStatus() { return status; }
    public String getRecipientId() { return recipientId; }
}
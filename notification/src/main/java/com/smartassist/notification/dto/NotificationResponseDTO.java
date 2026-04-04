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

    // 1. BOŞ CONSTRUCTOR EKLE (Test için kritik)
    public NotificationResponseDTO() {
    }

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

    // 2. SETTER METODLARINI EKLE (Testte verileri set edebilmen için)
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }
    public void setMessage(String message) { this.message = message; }
    public void setStatus(String status) { this.status = status; }
    public void setId(String id) { this.id = id; }

    // Getters
    public String getId() { return id; }
    public String getRecipientId() { return recipientId; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
}
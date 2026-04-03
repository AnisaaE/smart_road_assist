package com.smartassist.notification.dto;

import jakarta.validation.constraints.NotBlank; // Bu satırı ekle
import java.io.Serializable;

/**
 * Hiçbir kütüphaneye (Jakarta/Javax) bağımlı kalmadan 
 * sadece standart Java ile çalışan DTO.
 */
public class NotificationRequestDTO implements Serializable {

    @NotBlank(message = "Recipient ID cannot be empty") // Boş olamaz
    private String recipientId;

    private String requestId;

    @NotBlank(message = "Type cannot be empty") // Tip zorunlu
    private String type;

    @NotBlank(message = "Message cannot be empty") // Testin beklediği yer burası
    private String message;

    // ─── Constructors ─────────────────────────────────────────────────────────

    public NotificationRequestDTO() {
    }

    public NotificationRequestDTO(String recipientId, String requestId, String type, String message) {
        this.recipientId = recipientId;
        this.requestId = requestId;
        this.type = type;
        this.message = message;
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
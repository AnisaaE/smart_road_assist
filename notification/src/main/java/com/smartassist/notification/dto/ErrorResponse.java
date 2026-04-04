package com.smartassist.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map; // Bunu import etmeyi unutma

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;
    
    // Kırmızı yanan yer burasıydı, bu alanı ekliyoruz:
    private Map<String, String> errors; 
}
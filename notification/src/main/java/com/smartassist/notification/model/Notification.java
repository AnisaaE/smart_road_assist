package com.smartassist.notification.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data                          // getter + setter + toString + equals + hashCode
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;
    private String recipientId;
    private String requestId;
    private String type;
    private String message;
    private String status;
    private LocalDateTime sentAt;
}
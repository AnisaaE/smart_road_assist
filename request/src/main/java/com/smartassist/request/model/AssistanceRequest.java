package com.smartassist.request.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "assistance_requests")
public class AssistanceRequest {

    @Id
    private String id;
    private String userId;
    private RequestType type;
    private String description;
    private String location;
    private RequestStatus status;
    private String mechanicId;
    private Instant createdAt;
}

package com.smartassist.request.dto.response;

import java.time.Instant;

import com.smartassist.request.model.RequestStatus;
import com.smartassist.request.model.RequestType;

public record RequestResponse(
        String id,
        String userId,
        RequestType type,
        String description,
        String location,
        RequestStatus status,
        String mechanicId,
        Instant createdAt) {
}

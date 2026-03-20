package com.smartassist.request.dto.request;

import com.smartassist.request.model.RequestType;

public record CreateRequestRequest(
        String userId,
        RequestType type,
        String description,
        String location) {
}

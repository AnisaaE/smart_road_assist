package com.smartassist.request.dto.request;

import com.smartassist.request.model.RequestType;

public record UpdateRequestRequest(
        RequestType type,
        String description,
        String location) {
}

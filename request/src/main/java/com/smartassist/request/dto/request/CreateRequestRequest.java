package com.smartassist.request.dto.request;

import com.smartassist.request.model.RequestType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRequestRequest(
        @NotBlank(message = "userId is required")
        String userId,
        @NotNull(message = "type is required")
        RequestType type,
        @NotBlank(message = "description is required")
        String description,
        @NotBlank(message = "location is required")
        String location) {
}

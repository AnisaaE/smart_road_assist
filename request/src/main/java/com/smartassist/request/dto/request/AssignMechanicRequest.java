package com.smartassist.request.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AssignMechanicRequest(@NotBlank(message = "mechanicId is required") String mechanicId) {
}

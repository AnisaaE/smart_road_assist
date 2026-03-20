package com.smartassist.mechanic.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateMechanicRequest(
        @NotBlank(message = "name is required")
        String name,
        @NotBlank(message = "phoneNumber is required")
        String phoneNumber,
        @NotEmpty(message = "specialties are required")
        List<@NotBlank(message = "specialty is required") String> specialties,
        @NotBlank(message = "serviceArea is required")
        String serviceArea) {
}

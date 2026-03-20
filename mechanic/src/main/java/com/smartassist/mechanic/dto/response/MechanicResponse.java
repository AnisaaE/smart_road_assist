package com.smartassist.mechanic.dto.response;

import java.time.Instant;
import java.util.List;

public record MechanicResponse(
        String id,
        String name,
        String phoneNumber,
        List<String> specialties,
        String serviceArea,
        Instant createdAt) {
}

package com.smartassist.mechanic.dto.request;

import java.util.List;

public record UpdateMechanicRequest(
        String name,
        String phoneNumber,
        List<String> specialties,
        String serviceArea) {
}

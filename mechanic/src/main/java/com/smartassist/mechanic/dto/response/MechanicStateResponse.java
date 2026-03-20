package com.smartassist.mechanic.dto.response;

import com.smartassist.mechanic.model.MechanicStatus;

public record MechanicStateResponse(
        String mechanicId,
        MechanicStatus status,
        Double latitude,
        Double longitude) {
}

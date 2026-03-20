package com.smartassist.mechanic.mapper;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.smartassist.mechanic.dto.request.CreateMechanicRequest;
import com.smartassist.mechanic.dto.response.MechanicResponse;
import com.smartassist.mechanic.model.MechanicProfile;

@Component
public class MechanicMapper {

    public MechanicProfile toEntity(CreateMechanicRequest request, Instant createdAt) {
        return MechanicProfile.builder()
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .specialties(request.specialties())
                .serviceArea(request.serviceArea())
                .createdAt(createdAt)
                .build();
    }

    public MechanicResponse toResponse(MechanicProfile profile) {
        return new MechanicResponse(
                profile.getId(),
                profile.getName(),
                profile.getPhoneNumber(),
                profile.getSpecialties(),
                profile.getServiceArea(),
                profile.getCreatedAt());
    }
}

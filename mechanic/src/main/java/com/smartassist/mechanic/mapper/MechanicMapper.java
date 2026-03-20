package com.smartassist.mechanic.mapper;

import java.time.Instant;
import java.util.List;

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
                .specialties(copySpecialties(request.specialties()))
                .serviceArea(request.serviceArea())
                .createdAt(createdAt)
                .build();
    }

    public MechanicResponse toResponse(MechanicProfile profile) {
        return new MechanicResponse(
                profile.getId(),
                profile.getName(),
                profile.getPhoneNumber(),
                copySpecialties(profile.getSpecialties()),
                profile.getServiceArea(),
                profile.getCreatedAt());
    }

    private List<String> copySpecialties(List<String> specialties) {
        return specialties == null ? List.of() : List.copyOf(specialties);
    }
}

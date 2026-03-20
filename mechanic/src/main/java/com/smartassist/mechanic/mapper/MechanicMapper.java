package com.smartassist.mechanic.mapper;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Component;

import com.smartassist.mechanic.dto.request.CreateMechanicRequest;
import com.smartassist.mechanic.dto.request.UpdateMechanicStatusRequest;
import com.smartassist.mechanic.dto.request.UpdateMechanicRequest;
import com.smartassist.mechanic.dto.response.MechanicResponse;
import com.smartassist.mechanic.dto.response.MechanicStateResponse;
import com.smartassist.mechanic.model.MechanicLiveState;
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

    public List<MechanicResponse> toResponseList(List<MechanicProfile> profiles) {
        if (profiles == null) {
            return List.of();
        }

        return profiles.stream()
                .map(this::toResponse)
                .toList();
    }

    public void applyUpdate(MechanicProfile profile, UpdateMechanicRequest request) {
        profile.setName(request.name());
        profile.setPhoneNumber(request.phoneNumber());
        profile.setSpecialties(copySpecialties(request.specialties()));
        profile.setServiceArea(request.serviceArea());
    }

    public void applyStatusUpdate(MechanicLiveState liveState, UpdateMechanicStatusRequest request) {
        liveState.setStatus(request.status());
    }

    public MechanicStateResponse toStateResponse(MechanicLiveState liveState) {
        return new MechanicStateResponse(
                liveState.getMechanicId(),
                liveState.getStatus(),
                liveState.getLatitude(),
                liveState.getLongitude());
    }

    private List<String> copySpecialties(List<String> specialties) {
        return specialties == null ? List.of() : List.copyOf(specialties);
    }
}

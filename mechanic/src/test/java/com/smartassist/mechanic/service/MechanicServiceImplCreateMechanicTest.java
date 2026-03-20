package com.smartassist.mechanic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartassist.mechanic.dto.request.CreateMechanicRequest;
import com.smartassist.mechanic.dto.response.MechanicResponse;
import com.smartassist.mechanic.mapper.MechanicMapper;
import com.smartassist.mechanic.model.MechanicProfile;
import com.smartassist.mechanic.repository.MechanicRepository;
import com.smartassist.mechanic.service.impl.MechanicServiceImpl;

@ExtendWith(MockitoExtension.class)
class MechanicServiceImplCreateMechanicTest {

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private MechanicMapper mechanicMapper;

    @InjectMocks
    private MechanicServiceImpl mechanicService;

    @Test
    void createMechanicShouldPersistMechanicProfile() {
        CreateMechanicRequest request = new CreateMechanicRequest(
                "Jane Doe",
                "+90-555-0101",
                List.of("BATTERY", "TOWING"),
                "Istanbul");

        when(mechanicMapper.toEntity(eq(request), any(Instant.class))).thenAnswer(invocation ->
                MechanicProfile.builder()
                        .name(request.name())
                        .phoneNumber(request.phoneNumber())
                        .specialties(request.specialties())
                        .serviceArea(request.serviceArea())
                        .createdAt(invocation.getArgument(1))
                        .build());

        when(mechanicRepository.save(any(MechanicProfile.class))).thenAnswer(invocation -> {
            MechanicProfile profileToSave = invocation.getArgument(0);
            return MechanicProfile.builder()
                    .id("mech-1")
                    .name(profileToSave.getName())
                    .phoneNumber(profileToSave.getPhoneNumber())
                    .specialties(profileToSave.getSpecialties())
                    .serviceArea(profileToSave.getServiceArea())
                    .createdAt(profileToSave.getCreatedAt())
                    .build();
        });

        when(mechanicMapper.toResponse(any(MechanicProfile.class))).thenAnswer(invocation -> {
            MechanicProfile saved = invocation.getArgument(0);
            return new MechanicResponse(
                    saved.getId(),
                    saved.getName(),
                    saved.getPhoneNumber(),
                    saved.getSpecialties(),
                    saved.getServiceArea(),
                    saved.getCreatedAt());
        });

        MechanicResponse response = mechanicService.createMechanic(request);

        ArgumentCaptor<MechanicProfile> captor = ArgumentCaptor.forClass(MechanicProfile.class);
        verify(mechanicRepository).save(captor.capture());
        MechanicProfile persisted = captor.getValue();

        assertThat(persisted.getId()).isNull();
        assertThat(persisted.getName()).isEqualTo("Jane Doe");
        assertThat(persisted.getPhoneNumber()).isEqualTo("+90-555-0101");
        assertThat(persisted.getSpecialties()).containsExactly("BATTERY", "TOWING");
        assertThat(persisted.getServiceArea()).isEqualTo("Istanbul");
        assertThat(persisted.getCreatedAt()).isBeforeOrEqualTo(Instant.now());

        assertThat(response.id()).isEqualTo("mech-1");
        assertThat(response.name()).isEqualTo("Jane Doe");
        assertThat(response.specialties()).containsExactly("BATTERY", "TOWING");
    }
}

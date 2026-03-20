package com.smartassist.mechanic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartassist.mechanic.dto.request.UpdateMechanicRequest;
import com.smartassist.mechanic.dto.response.MechanicResponse;
import com.smartassist.mechanic.mapper.MechanicMapper;
import com.smartassist.mechanic.model.MechanicProfile;
import com.smartassist.mechanic.repository.MechanicRepository;
import com.smartassist.mechanic.service.impl.MechanicServiceImpl;

@ExtendWith(MockitoExtension.class)
class MechanicServiceImplUpdateMechanicTest {

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private MechanicMapper mechanicMapper;

    @InjectMocks
    private MechanicServiceImpl mechanicService;

    @Test
    void updateMechanicShouldPersistUpdatedFields() {
        MechanicProfile existingProfile = MechanicProfile.builder()
                .id("mech-1")
                .name("Jane Doe")
                .phoneNumber("+90-555-0101")
                .specialties(List.of("BATTERY"))
                .serviceArea("Istanbul")
                .createdAt(Instant.parse("2026-03-20T10:15:30Z"))
                .build();

        UpdateMechanicRequest update = new UpdateMechanicRequest(
                "Jane Smith",
                "+90-555-0202",
                List.of("TOWING", "LOCKOUT"),
                "Ankara");

        when(mechanicRepository.findById("mech-1")).thenReturn(Optional.of(existingProfile));
        when(mechanicRepository.save(any(MechanicProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doAnswer(invocation -> {
            MechanicProfile profileToUpdate = invocation.getArgument(0);
            UpdateMechanicRequest updateRequest = invocation.getArgument(1);
            profileToUpdate.setName(updateRequest.name());
            profileToUpdate.setPhoneNumber(updateRequest.phoneNumber());
            profileToUpdate.setSpecialties(updateRequest.specialties());
            profileToUpdate.setServiceArea(updateRequest.serviceArea());
            return null;
        }).when(mechanicMapper).applyUpdate(any(MechanicProfile.class), any(UpdateMechanicRequest.class));
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

        MechanicResponse response = mechanicService.updateMechanic("mech-1", update);

        ArgumentCaptor<MechanicProfile> captor = ArgumentCaptor.forClass(MechanicProfile.class);
        verify(mechanicRepository).save(captor.capture());
        MechanicProfile savedProfile = captor.getValue();

        assertThat(savedProfile.getId()).isEqualTo("mech-1");
        assertThat(savedProfile.getName()).isEqualTo("Jane Smith");
        assertThat(savedProfile.getPhoneNumber()).isEqualTo("+90-555-0202");
        assertThat(savedProfile.getSpecialties()).containsExactly("TOWING", "LOCKOUT");
        assertThat(savedProfile.getServiceArea()).isEqualTo("Ankara");
        assertThat(savedProfile.getCreatedAt()).isEqualTo(Instant.parse("2026-03-20T10:15:30Z"));

        assertThat(response.id()).isEqualTo("mech-1");
        assertThat(response.name()).isEqualTo("Jane Smith");
        assertThat(response.specialties()).containsExactly("TOWING", "LOCKOUT");
    }
}

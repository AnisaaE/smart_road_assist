package com.smartassist.mechanic.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartassist.mechanic.mapper.MechanicMapper;
import com.smartassist.mechanic.model.MechanicProfile;
import com.smartassist.mechanic.repository.MechanicRepository;
import com.smartassist.mechanic.service.impl.MechanicServiceImpl;

@ExtendWith(MockitoExtension.class)
class MechanicServiceImplDeleteMechanicTest {

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private MechanicMapper mechanicMapper;

    @InjectMocks
    private MechanicServiceImpl mechanicService;

    @Test
    void deleteMechanicShouldRemoveExistingProfile() {
        MechanicProfile profile = MechanicProfile.builder()
                .id("mech-1")
                .name("Jane Doe")
                .phoneNumber("+90-555-0101")
                .specialties(List.of("BATTERY"))
                .serviceArea("Istanbul")
                .createdAt(Instant.parse("2026-03-20T10:15:30Z"))
                .build();

        when(mechanicRepository.findById("mech-1")).thenReturn(Optional.of(profile));

        mechanicService.deleteMechanic("mech-1");

        verify(mechanicRepository).delete(profile);
    }
}

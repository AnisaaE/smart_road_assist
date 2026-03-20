package com.smartassist.mechanic.service;

import static org.assertj.core.api.Assertions.assertThat;
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

import com.smartassist.mechanic.dto.response.MechanicResponse;
import com.smartassist.mechanic.mapper.MechanicMapper;
import com.smartassist.mechanic.model.MechanicProfile;
import com.smartassist.mechanic.repository.MechanicRepository;
import com.smartassist.mechanic.service.impl.MechanicServiceImpl;

@ExtendWith(MockitoExtension.class)
class MechanicServiceImplGetMechanicByIdTest {

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private MechanicMapper mechanicMapper;

    @InjectMocks
    private MechanicServiceImpl mechanicService;

    @Test
    void getMechanicByIdShouldReturnMappedProfile() {
        MechanicProfile profile = MechanicProfile.builder()
                .id("mech-1")
                .name("Jane Doe")
                .phoneNumber("+90-555-0101")
                .specialties(List.of("BATTERY"))
                .serviceArea("Istanbul")
                .createdAt(Instant.parse("2026-03-20T10:15:30Z"))
                .build();

        MechanicResponse response = new MechanicResponse(
                "mech-1",
                "Jane Doe",
                "+90-555-0101",
                List.of("BATTERY"),
                "Istanbul",
                Instant.parse("2026-03-20T10:15:30Z"));

        when(mechanicRepository.findById("mech-1")).thenReturn(Optional.of(profile));
        when(mechanicMapper.toResponse(profile)).thenReturn(response);

        MechanicResponse result = mechanicService.getMechanicById("mech-1");

        verify(mechanicRepository).findById("mech-1");
        verify(mechanicMapper).toResponse(profile);
        assertThat(result).isEqualTo(response);
    }
}

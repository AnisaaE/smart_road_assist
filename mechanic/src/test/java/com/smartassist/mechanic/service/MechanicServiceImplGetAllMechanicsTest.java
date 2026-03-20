package com.smartassist.mechanic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

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
class MechanicServiceImplGetAllMechanicsTest {

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private MechanicMapper mechanicMapper;

    @InjectMocks
    private MechanicServiceImpl mechanicService;

    @Test
    void getAllMechanicsShouldReturnMappedProfiles() {
        List<MechanicProfile> profiles = List.of(
                MechanicProfile.builder()
                        .id("mech-1")
                        .name("Jane Doe")
                        .phoneNumber("+90-555-0101")
                        .specialties(List.of("BATTERY"))
                        .serviceArea("Istanbul")
                        .createdAt(Instant.parse("2026-03-20T10:15:30Z"))
                        .build(),
                MechanicProfile.builder()
                        .id("mech-2")
                        .name("John Doe")
                        .phoneNumber("+90-555-0102")
                        .specialties(List.of("TOWING"))
                        .serviceArea("Ankara")
                        .createdAt(Instant.parse("2026-03-20T10:16:30Z"))
                        .build());

        List<MechanicResponse> responses = List.of(
                new MechanicResponse(
                        "mech-1",
                        "Jane Doe",
                        "+90-555-0101",
                        List.of("BATTERY"),
                        "Istanbul",
                        Instant.parse("2026-03-20T10:15:30Z")),
                new MechanicResponse(
                        "mech-2",
                        "John Doe",
                        "+90-555-0102",
                        List.of("TOWING"),
                        "Ankara",
                        Instant.parse("2026-03-20T10:16:30Z")));

        when(mechanicRepository.findAll()).thenReturn(profiles);
        when(mechanicMapper.toResponseList(profiles)).thenReturn(responses);

        List<MechanicResponse> result = mechanicService.getAllMechanics();

        verify(mechanicRepository).findAll();
        verify(mechanicMapper).toResponseList(profiles);
        assertThat(result).containsExactlyElementsOf(responses);
    }
}

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

import com.smartassist.mechanic.dto.request.UpdateMechanicLocationRequest;
import com.smartassist.mechanic.dto.response.MechanicStateResponse;
import com.smartassist.mechanic.mapper.MechanicMapper;
import com.smartassist.mechanic.model.MechanicLiveState;
import com.smartassist.mechanic.model.MechanicProfile;
import com.smartassist.mechanic.model.MechanicStatus;
import com.smartassist.mechanic.repository.MechanicLiveStateRepository;
import com.smartassist.mechanic.repository.MechanicRepository;
import com.smartassist.mechanic.service.impl.MechanicServiceImpl;

@ExtendWith(MockitoExtension.class)
class MechanicServiceImplUpdateMechanicLocationTest {

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private MechanicLiveStateRepository mechanicLiveStateRepository;

    @Mock
    private MechanicMapper mechanicMapper;

    @InjectMocks
    private MechanicServiceImpl mechanicService;

    @Test
    void updateMechanicLocationShouldPersistLiveStateCoordinates() {
        MechanicProfile profile = MechanicProfile.builder()
                .id("mech-1")
                .name("Jane Doe")
                .phoneNumber("+90-555-0101")
                .specialties(List.of("BATTERY"))
                .serviceArea("Istanbul")
                .createdAt(Instant.parse("2026-03-20T10:15:30Z"))
                .build();

        MechanicLiveState existingState = MechanicLiveState.builder()
                .mechanicId("mech-1")
                .status(MechanicStatus.AVAILABLE)
                .latitude(41.0082)
                .longitude(28.9784)
                .build();

        MechanicLiveState savedState = MechanicLiveState.builder()
                .mechanicId("mech-1")
                .status(MechanicStatus.AVAILABLE)
                .latitude(39.9334)
                .longitude(32.8597)
                .build();

        MechanicStateResponse response = new MechanicStateResponse(
                "mech-1",
                MechanicStatus.AVAILABLE,
                39.9334,
                32.8597);

        when(mechanicRepository.findById("mech-1")).thenReturn(Optional.of(profile));
        when(mechanicLiveStateRepository.findById("mech-1")).thenReturn(Optional.of(existingState));
        when(mechanicLiveStateRepository.save(existingState)).thenReturn(savedState);
        when(mechanicMapper.toStateResponse(savedState)).thenReturn(response);

        MechanicStateResponse result = mechanicService.updateMechanicLocation(
                "mech-1",
                new UpdateMechanicLocationRequest(39.9334, 32.8597));

        verify(mechanicLiveStateRepository).save(existingState);
        assertThat(existingState.getLatitude()).isEqualTo(39.9334);
        assertThat(existingState.getLongitude()).isEqualTo(32.8597);
        assertThat(result.latitude()).isEqualTo(39.9334);
        assertThat(result.longitude()).isEqualTo(32.8597);
    }
}

package com.smartassist.request.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartassist.request.dto.request.AssignMechanicRequest;
import com.smartassist.request.dto.response.RequestResponse;
import com.smartassist.request.mapper.RequestMapper;
import com.smartassist.request.model.AssistanceRequest;
import com.smartassist.request.model.RequestStatus;
import com.smartassist.request.model.RequestType;
import com.smartassist.request.repository.RequestRepository;
import com.smartassist.request.service.impl.RequestServiceImpl;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplAssignMechanicTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper requestMapper;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Test
    void assignMechanicShouldSetMechanicAndAssignedStatus() {
        AssistanceRequest existingRequest = AssistanceRequest.builder()
                .id("req-1")
                .userId("user-123")
                .type(RequestType.BATTERY)
                .description("Battery is dead")
                .location("Downtown garage")
                .status(RequestStatus.CREATED)
                .createdAt(Instant.parse("2026-03-20T10:15:30Z"))
                .build();

        AssignMechanicRequest assignRequest = new AssignMechanicRequest("mech-42");

        when(requestRepository.findById("req-1")).thenReturn(Optional.of(existingRequest));
        when(requestRepository.save(any(AssistanceRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(requestMapper.toResponse(any(AssistanceRequest.class))).thenAnswer(invocation -> {
            AssistanceRequest saved = invocation.getArgument(0);
            return new RequestResponse(
                    saved.getId(),
                    saved.getUserId(),
                    saved.getType(),
                    saved.getDescription(),
                    saved.getLocation(),
                    saved.getStatus(),
                    saved.getMechanicId(),
                    saved.getCreatedAt());
        });

        RequestResponse response = requestService.assignMechanic("req-1", assignRequest);

        ArgumentCaptor<AssistanceRequest> captor = ArgumentCaptor.forClass(AssistanceRequest.class);
        verify(requestRepository).save(captor.capture());
        AssistanceRequest savedRequest = captor.getValue();

        assertThat(savedRequest.getMechanicId()).isEqualTo("mech-42");
        assertThat(savedRequest.getStatus()).isEqualTo(RequestStatus.ASSIGNED);
        assertThat(response.mechanicId()).isEqualTo("mech-42");
        assertThat(response.status()).isEqualTo(RequestStatus.ASSIGNED);
    }
}

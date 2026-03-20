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

import com.smartassist.request.dto.request.UpdateRequestRequest;
import com.smartassist.request.dto.response.RequestResponse;
import com.smartassist.request.mapper.RequestMapper;
import com.smartassist.request.model.AssistanceRequest;
import com.smartassist.request.model.RequestStatus;
import com.smartassist.request.model.RequestType;
import com.smartassist.request.repository.RequestRepository;
import com.smartassist.request.service.impl.RequestServiceImpl;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplUpdateRequestTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper requestMapper;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Test
    void updateRequestShouldPersistUpdatedFields() {
        AssistanceRequest existingRequest = AssistanceRequest.builder()
                .id("req-1")
                .userId("user-123")
                .type(RequestType.BATTERY)
                .description("Battery is dead")
                .location("Downtown garage")
                .status(RequestStatus.CREATED)
                .createdAt(Instant.parse("2026-03-20T10:15:30Z"))
                .build();

        UpdateRequestRequest update = new UpdateRequestRequest(
                RequestType.TOW,
                "Vehicle needs towing",
                "Highway 5");

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

        RequestResponse response = requestService.updateRequest("req-1", update);

        ArgumentCaptor<AssistanceRequest> captor = ArgumentCaptor.forClass(AssistanceRequest.class);
        verify(requestRepository).save(captor.capture());
        AssistanceRequest savedRequest = captor.getValue();

        assertThat(savedRequest.getId()).isEqualTo("req-1");
        assertThat(savedRequest.getUserId()).isEqualTo("user-123");
        assertThat(savedRequest.getType()).isEqualTo(RequestType.TOW);
        assertThat(savedRequest.getDescription()).isEqualTo("Vehicle needs towing");
        assertThat(savedRequest.getLocation()).isEqualTo("Highway 5");
        assertThat(savedRequest.getStatus()).isEqualTo(RequestStatus.CREATED);
        assertThat(savedRequest.getCreatedAt()).isEqualTo(Instant.parse("2026-03-20T10:15:30Z"));

        assertThat(response.id()).isEqualTo("req-1");
        assertThat(response.type()).isEqualTo(RequestType.TOW);
        assertThat(response.description()).isEqualTo("Vehicle needs towing");
    }
}

package com.smartassist.request.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
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

import com.smartassist.request.dto.request.UpdateRequestStatusRequest;
import com.smartassist.request.exception.InvalidRequestStateException;
import com.smartassist.request.dto.response.RequestResponse;
import com.smartassist.request.mapper.RequestMapper;
import com.smartassist.request.model.AssistanceRequest;
import com.smartassist.request.model.RequestStatus;
import com.smartassist.request.model.RequestType;
import com.smartassist.request.repository.RequestRepository;
import com.smartassist.request.service.impl.RequestServiceImpl;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplUpdateStatusTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper requestMapper;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Test
    void updateStatusShouldPersistNewStatus() {
        AssistanceRequest existingRequest = AssistanceRequest.builder()
                .id("req-1")
                .userId("user-123")
                .type(RequestType.BATTERY)
                .description("Battery is dead")
                .location("Downtown garage")
                .status(RequestStatus.ASSIGNED)
                .mechanicId("mech-42")
                .createdAt(Instant.parse("2026-03-20T10:15:30Z"))
                .build();

        UpdateRequestStatusRequest statusRequest = new UpdateRequestStatusRequest(RequestStatus.IN_PROGRESS);

        when(requestRepository.findById("req-1")).thenReturn(Optional.of(existingRequest));
        when(requestRepository.save(any(AssistanceRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doAnswer(invocation -> {
            AssistanceRequest requestToUpdate = invocation.getArgument(0);
            UpdateRequestStatusRequest updateRequest = invocation.getArgument(1);
            requestToUpdate.setStatus(updateRequest.status());
            return null;
        }).when(requestMapper).applyStatusUpdate(any(AssistanceRequest.class), any(UpdateRequestStatusRequest.class));
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

        RequestResponse response = requestService.updateStatus("req-1", statusRequest);

        ArgumentCaptor<AssistanceRequest> captor = ArgumentCaptor.forClass(AssistanceRequest.class);
        verify(requestRepository).save(captor.capture());
        AssistanceRequest savedRequest = captor.getValue();

        assertThat(savedRequest.getStatus()).isEqualTo(RequestStatus.IN_PROGRESS);
        assertThat(savedRequest.getMechanicId()).isEqualTo("mech-42");
        assertThat(response.status()).isEqualTo(RequestStatus.IN_PROGRESS);
    }

    @Test
    void updateStatusShouldRejectTransitionFromDoneToInProgress() {
        AssistanceRequest existingRequest = AssistanceRequest.builder()
                .id("req-1")
                .userId("user-123")
                .type(RequestType.BATTERY)
                .description("Battery is dead")
                .location("Downtown garage")
                .status(RequestStatus.DONE)
                .mechanicId("mech-42")
                .createdAt(Instant.parse("2026-03-20T10:15:30Z"))
                .build();

        when(requestRepository.findById("req-1")).thenReturn(Optional.of(existingRequest));

        assertThatThrownBy(() -> requestService.updateStatus(
                "req-1",
                new UpdateRequestStatusRequest(RequestStatus.IN_PROGRESS)))
                .isInstanceOf(InvalidRequestStateException.class)
                .hasMessage("Cannot change request status from DONE to IN_PROGRESS");

        verify(requestRepository, never()).save(any(AssistanceRequest.class));
    }

    @Test
    void updateStatusShouldRejectTransitionFromCreatedToDone() {
        AssistanceRequest existingRequest = AssistanceRequest.builder()
                .id("req-1")
                .userId("user-123")
                .type(RequestType.BATTERY)
                .description("Battery is dead")
                .location("Downtown garage")
                .status(RequestStatus.CREATED)
                .createdAt(Instant.parse("2026-03-20T10:15:30Z"))
                .build();

        when(requestRepository.findById("req-1")).thenReturn(Optional.of(existingRequest));

        assertThatThrownBy(() -> requestService.updateStatus(
                "req-1",
                new UpdateRequestStatusRequest(RequestStatus.DONE)))
                .isInstanceOf(InvalidRequestStateException.class)
                .hasMessage("Cannot change request status from CREATED to DONE");

        verify(requestRepository, never()).save(any(AssistanceRequest.class));
    }
}

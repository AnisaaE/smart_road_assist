package com.smartassist.request.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartassist.request.dto.request.CreateRequestRequest;
import com.smartassist.request.dto.response.RequestResponse;
import com.smartassist.request.mapper.RequestMapper;
import com.smartassist.request.model.AssistanceRequest;
import com.smartassist.request.model.RequestStatus;
import com.smartassist.request.model.RequestType;
import com.smartassist.request.repository.RequestRepository;
import com.smartassist.request.service.impl.RequestServiceImpl;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplCreateRequestTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper requestMapper;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Test
    void createRequestShouldPersistRequestWithCreatedStatus() {
        CreateRequestRequest request = new CreateRequestRequest(
                "user-123",
                RequestType.BATTERY,
                "Battery is dead",
                "Downtown garage");

        when(requestRepository.save(any(AssistanceRequest.class))).thenAnswer(invocation -> {
            AssistanceRequest requestToSave = invocation.getArgument(0);
            return AssistanceRequest.builder()
                    .id("req-1")
                    .userId(requestToSave.getUserId())
                    .type(requestToSave.getType())
                    .description(requestToSave.getDescription())
                    .location(requestToSave.getLocation())
                    .status(requestToSave.getStatus())
                    .createdAt(requestToSave.getCreatedAt())
                    .build();
        });

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

        RequestResponse response = requestService.createRequest(request);

        ArgumentCaptor<AssistanceRequest> captor = ArgumentCaptor.forClass(AssistanceRequest.class);
        verify(requestRepository).save(captor.capture());
        AssistanceRequest persisted = captor.getValue();

        assertThat(persisted.getId()).isNull();
        assertThat(persisted.getUserId()).isEqualTo("user-123");
        assertThat(persisted.getType()).isEqualTo(RequestType.BATTERY);
        assertThat(persisted.getDescription()).isEqualTo("Battery is dead");
        assertThat(persisted.getLocation()).isEqualTo("Downtown garage");
        assertThat(persisted.getStatus()).isEqualTo(RequestStatus.CREATED);
        assertThat(persisted.getMechanicId()).isNull();
        assertThat(persisted.getCreatedAt()).isBeforeOrEqualTo(Instant.now());

        assertThat(response.id()).isEqualTo("req-1");
        assertThat(response.status()).isEqualTo(RequestStatus.CREATED);
    }
}

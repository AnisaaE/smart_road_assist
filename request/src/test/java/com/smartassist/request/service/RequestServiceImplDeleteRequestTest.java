package com.smartassist.request.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartassist.request.mapper.RequestMapper;
import com.smartassist.request.model.AssistanceRequest;
import com.smartassist.request.model.RequestStatus;
import com.smartassist.request.model.RequestType;
import com.smartassist.request.repository.RequestRepository;
import com.smartassist.request.service.impl.RequestServiceImpl;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplDeleteRequestTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper requestMapper;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Test
    void deleteRequestShouldRemoveExistingRequest() {
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

        requestService.deleteRequest("req-1");

        verify(requestRepository).delete(existingRequest);
    }
}

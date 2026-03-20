package com.smartassist.request.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.smartassist.request.dto.request.CreateRequestRequest;
import com.smartassist.request.dto.response.RequestResponse;
import com.smartassist.request.mapper.RequestMapper;
import com.smartassist.request.model.AssistanceRequest;
import com.smartassist.request.model.RequestStatus;
import com.smartassist.request.repository.RequestRepository;
import com.smartassist.request.service.RequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Override
    public RequestResponse createRequest(CreateRequestRequest request) {
        AssistanceRequest assistanceRequest = AssistanceRequest.builder()
                .userId(request.userId())
                .type(request.type())
                .description(request.description())
                .location(request.location())
                .status(RequestStatus.CREATED)
                .createdAt(Instant.now())
                .build();

        AssistanceRequest savedRequest = requestRepository.save(assistanceRequest);
        return requestMapper.toResponse(savedRequest);
    }
}

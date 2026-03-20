package com.smartassist.request.service.impl;

import java.time.Instant;
import java.util.List;

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
        AssistanceRequest assistanceRequest = requestMapper.toEntity(request, Instant.now());
        AssistanceRequest savedRequest = requestRepository.save(assistanceRequest);
        return requestMapper.toResponse(savedRequest);
    }

    @Override
    public List<RequestResponse> getAllRequests() {
        return requestMapper.toResponseList(requestRepository.findAll());
    }

    @Override
    public RequestResponse getRequestById(String id) {
        return requestRepository.findById(id)
                .map(requestMapper::toResponse)
                .orElse(null);
    }
}

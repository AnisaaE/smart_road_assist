package com.smartassist.request.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.smartassist.request.dto.request.AssignMechanicRequest;
import com.smartassist.request.dto.request.CreateRequestRequest;
import com.smartassist.request.dto.request.UpdateRequestStatusRequest;
import com.smartassist.request.dto.request.UpdateRequestRequest;
import com.smartassist.request.dto.response.RequestResponse;
import com.smartassist.request.exception.InvalidRequestStateException;
import com.smartassist.request.mapper.RequestMapper;
import com.smartassist.request.exception.RequestNotFoundException;
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
        AssistanceRequest request = findRequestOrThrow(id);
        return requestMapper.toResponse(request);
    }

    @Override
    public RequestResponse updateRequest(String id, UpdateRequestRequest request) {
        AssistanceRequest existingRequest = findRequestOrThrow(id);
        requestMapper.applyUpdate(existingRequest, request);

        AssistanceRequest updatedRequest = requestRepository.save(existingRequest);
        return requestMapper.toResponse(updatedRequest);
    }

    @Override
    public void deleteRequest(String id) {
        requestRepository.delete(findRequestOrThrow(id));
    }

    @Override
    public RequestResponse assignMechanic(String id, AssignMechanicRequest request) {
        AssistanceRequest existingRequest = findRequestOrThrow(id);
        validateAssignment(existingRequest);
        requestMapper.applyAssignment(existingRequest, request);

        AssistanceRequest updatedRequest = requestRepository.save(existingRequest);
        return requestMapper.toResponse(updatedRequest);
    }

    @Override
    public RequestResponse updateStatus(String id, UpdateRequestStatusRequest request) {
        AssistanceRequest existingRequest = findRequestOrThrow(id);
        validateStatusTransition(existingRequest.getStatus(), request.status());
        requestMapper.applyStatusUpdate(existingRequest, request);

        AssistanceRequest updatedRequest = requestRepository.save(existingRequest);
        return requestMapper.toResponse(updatedRequest);
    }

    private AssistanceRequest findRequestOrThrow(String id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(buildNotFoundMessage(id)));
    }

    private String buildNotFoundMessage(String id) {
        return "Request not found: " + id;
    }

    private void validateStatusTransition(RequestStatus currentStatus, RequestStatus newStatus) {
        if (currentStatus == RequestStatus.DONE && newStatus == RequestStatus.IN_PROGRESS) {
            throw new InvalidRequestStateException(buildInvalidTransitionMessage(currentStatus, newStatus));
        }
    }

    private String buildInvalidTransitionMessage(RequestStatus currentStatus, RequestStatus newStatus) {
        return "Cannot change request status from " + currentStatus + " to " + newStatus;
    }

    private void validateAssignment(AssistanceRequest request) {
        if (request.getStatus() == RequestStatus.CANCELLED) {
            throw new InvalidRequestStateException("Cannot assign mechanic to request with status CANCELLED");
        }
    }
}

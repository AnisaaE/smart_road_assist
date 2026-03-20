package com.smartassist.request.mapper;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.smartassist.request.dto.request.CreateRequestRequest;
import com.smartassist.request.dto.response.RequestResponse;
import com.smartassist.request.model.AssistanceRequest;
import com.smartassist.request.model.RequestStatus;

@Component
public class RequestMapper {

    public AssistanceRequest toEntity(CreateRequestRequest request, Instant createdAt) {
        return AssistanceRequest.builder()
                .userId(request.userId())
                .type(request.type())
                .description(request.description())
                .location(request.location())
                .status(RequestStatus.CREATED)
                .createdAt(createdAt)
                .build();
    }

    public RequestResponse toResponse(AssistanceRequest request) {
        return new RequestResponse(
                request.getId(),
                request.getUserId(),
                request.getType(),
                request.getDescription(),
                request.getLocation(),
                request.getStatus(),
                request.getMechanicId(),
                request.getCreatedAt());
    }
}

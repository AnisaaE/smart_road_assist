package com.smartassist.request.mapper;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Component;

import com.smartassist.request.dto.request.AssignMechanicRequest;
import com.smartassist.request.dto.request.CreateRequestRequest;
import com.smartassist.request.dto.request.UpdateRequestStatusRequest;
import com.smartassist.request.dto.request.UpdateRequestRequest;
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

    public List<RequestResponse> toResponseList(List<AssistanceRequest> requests) {
        return requests.stream()
                .map(this::toResponse)
                .toList();
    }

    public void applyUpdate(AssistanceRequest request, UpdateRequestRequest updateRequest) {
        request.setType(updateRequest.type());
        request.setDescription(updateRequest.description());
        request.setLocation(updateRequest.location());
    }

    public void applyAssignment(AssistanceRequest request, AssignMechanicRequest assignRequest) {
        request.setMechanicId(assignRequest.mechanicId());
        request.setStatus(RequestStatus.ASSIGNED);
    }

    public void applyStatusUpdate(AssistanceRequest request, UpdateRequestStatusRequest statusRequest) {
        request.setStatus(statusRequest.status());
    }
}

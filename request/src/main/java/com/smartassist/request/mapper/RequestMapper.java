package com.smartassist.request.mapper;

import org.springframework.stereotype.Component;

import com.smartassist.request.dto.response.RequestResponse;
import com.smartassist.request.model.AssistanceRequest;

@Component
public class RequestMapper {

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

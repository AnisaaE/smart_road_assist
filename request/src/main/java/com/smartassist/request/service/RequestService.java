package com.smartassist.request.service;

import java.util.List;

import com.smartassist.request.dto.request.CreateRequestRequest;
import com.smartassist.request.dto.request.UpdateRequestRequest;
import com.smartassist.request.dto.response.RequestResponse;

public interface RequestService {

    RequestResponse createRequest(CreateRequestRequest request);

    List<RequestResponse> getAllRequests();

    RequestResponse getRequestById(String id);

    RequestResponse updateRequest(String id, UpdateRequestRequest request);
}

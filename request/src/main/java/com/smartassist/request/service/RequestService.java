package com.smartassist.request.service;

import com.smartassist.request.dto.request.CreateRequestRequest;
import com.smartassist.request.dto.response.RequestResponse;

public interface RequestService {

    RequestResponse createRequest(CreateRequestRequest request);
}

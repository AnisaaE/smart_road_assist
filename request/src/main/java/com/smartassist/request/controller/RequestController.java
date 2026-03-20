package com.smartassist.request.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smartassist.request.dto.request.CreateRequestRequest;
import com.smartassist.request.dto.request.UpdateRequestRequest;
import com.smartassist.request.dto.response.RequestResponse;
import com.smartassist.request.service.RequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping(RequestController.REQUESTS_PATH)
@RequiredArgsConstructor
public class RequestController {

    static final String REQUESTS_PATH = "/requests";
    private static final String REQUEST_BY_ID_PATH = "/{id}";

    private final RequestService requestService;

    @GetMapping
    public List<RequestResponse> getAllRequests() {
        return requestService.getAllRequests();
    }

    @GetMapping(REQUEST_BY_ID_PATH)
    public RequestResponse getRequestById(@PathVariable String id) {
        return requestService.getRequestById(id);
    }

    @PutMapping(REQUEST_BY_ID_PATH)
    public RequestResponse updateRequest(@PathVariable String id, @RequestBody UpdateRequestRequest request) {
        return requestService.updateRequest(id, request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponse createRequest(@Valid @RequestBody CreateRequestRequest request) {
        return requestService.createRequest(request);
    }
}

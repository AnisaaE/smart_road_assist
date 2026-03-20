package com.smartassist.request.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smartassist.request.dto.request.AssignMechanicRequest;
import com.smartassist.request.dto.request.CreateRequestRequest;
import com.smartassist.request.dto.request.UpdateRequestStatusRequest;
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
    private static final String REQUEST_ID_PATH = "/{id}";
    private static final String ASSIGN_PATH = REQUEST_ID_PATH + "/assign";
    private static final String UPDATE_STATUS_PATH = REQUEST_ID_PATH + "/status";

    private final RequestService requestService;

    @GetMapping
    public List<RequestResponse> getAllRequests() {
        return requestService.getAllRequests();
    }

    @GetMapping(REQUEST_ID_PATH)
    public RequestResponse getRequestById(@PathVariable String id) {
        return requestService.getRequestById(id);
    }

    @PutMapping(REQUEST_ID_PATH)
    public RequestResponse updateRequest(@PathVariable String id, @RequestBody UpdateRequestRequest request) {
        return requestService.updateRequest(id, request);
    }

    @DeleteMapping(REQUEST_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequest(@PathVariable String id) {
        requestService.deleteRequest(id);
    }

    @PutMapping(ASSIGN_PATH)
    public RequestResponse assignMechanic(@PathVariable String id, @Valid @RequestBody AssignMechanicRequest request) {
        return requestService.assignMechanic(id, request);
    }

    @PutMapping(UPDATE_STATUS_PATH)
    public RequestResponse updateStatus(@PathVariable String id, @RequestBody UpdateRequestStatusRequest request) {
        return requestService.updateStatus(id, request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponse createRequest(@Valid @RequestBody CreateRequestRequest request) {
        return requestService.createRequest(request);
    }
}

package com.smartassist.request.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smartassist.request.dto.request.CreateRequestRequest;
import com.smartassist.request.dto.response.RequestResponse;
import com.smartassist.request.service.RequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(RequestController.REQUESTS_PATH)
@RequiredArgsConstructor
public class RequestController {

    static final String REQUESTS_PATH = "/requests";

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponse createRequest(@Valid @RequestBody CreateRequestRequest request) {
        return requestService.createRequest(request);
    }
}

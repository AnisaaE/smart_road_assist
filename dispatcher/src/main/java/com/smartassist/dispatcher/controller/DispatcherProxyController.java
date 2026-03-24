package com.smartassist.dispatcher.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartassist.dispatcher.service.DispatcherProxyService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DispatcherProxyController {

    private static final String REQUESTS_PATH = "/api/requests";
    private static final String MECHANICS_PATH = "/api/mechanics";

    private final DispatcherProxyService dispatcherProxyService;

    @RequestMapping({REQUESTS_PATH, REQUESTS_PATH + "/**"})
    public ResponseEntity<byte[]> proxyRequestService(HttpServletRequest request,
                                                      @RequestBody(required = false) byte[] requestBody) {
        return dispatcherProxyService.forwardToRequestService(request, requestBody);
    }

    @RequestMapping({MECHANICS_PATH, MECHANICS_PATH + "/**"})
    public ResponseEntity<byte[]> proxyMechanicService(HttpServletRequest request,
                                                       @RequestBody(required = false) byte[] requestBody) {
        return dispatcherProxyService.forwardToMechanicService(request, requestBody);
    }
}

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

    private final DispatcherProxyService dispatcherProxyService;

    @RequestMapping({"/api/requests", "/api/requests/**"})
    public ResponseEntity<byte[]> proxyRequestService(HttpServletRequest request,
                                                      @RequestBody(required = false) byte[] requestBody) {
        return dispatcherProxyService.forwardToRequestService(request, requestBody);
    }

    @RequestMapping({"/api/mechanics", "/api/mechanics/**"})
    public ResponseEntity<byte[]> proxyMechanicService(HttpServletRequest request,
                                                       @RequestBody(required = false) byte[] requestBody) {
        return dispatcherProxyService.forwardToMechanicService(request, requestBody);
    }
}

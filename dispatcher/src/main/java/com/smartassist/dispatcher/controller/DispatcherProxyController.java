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
    private static final String USERS_PATH = "/api/users";
    private static final String PAYMENTS_PATH = "/api/payments";
    private static final String NOTIFICATIONS_PATH = "/api/notifications";

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

    @RequestMapping({USERS_PATH, USERS_PATH + "/**"})
    public ResponseEntity<byte[]> proxyUserService(HttpServletRequest request,
                                                   @RequestBody(required = false) byte[] requestBody) {
        return dispatcherProxyService.forwardToUserService(request, requestBody);
    }

    @RequestMapping({PAYMENTS_PATH, PAYMENTS_PATH + "/**"})
    public ResponseEntity<byte[]> proxyPaymentService(HttpServletRequest request,
                                                      @RequestBody(required = false) byte[] requestBody) {
        return dispatcherProxyService.forwardToPaymentService(request, requestBody);
    }

    @RequestMapping({NOTIFICATIONS_PATH, NOTIFICATIONS_PATH + "/**"})
    public ResponseEntity<byte[]> proxyNotificationService(HttpServletRequest request,
                                                           @RequestBody(required = false) byte[] requestBody) {
        return dispatcherProxyService.forwardToNotificationService(request, requestBody);
    }
}

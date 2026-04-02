package com.smartassist.dispatcher.service;

import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.ResourceAccessException;

import com.smartassist.dispatcher.config.DispatcherAuthorizationFilter;
import com.smartassist.dispatcher.config.DispatcherProperties;
import com.smartassist.dispatcher.exception.ServiceUnavailableException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DispatcherProxyService {

    private static final String API_PREFIX = "/api";
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";

    private final RestClient restClient;
    private final DispatcherProperties dispatcherProperties;

    public ResponseEntity<byte[]> forwardToRequestService(HttpServletRequest request, byte[] requestBody) {
        return forward(request, requestBody, dispatcherProperties.requestServiceUrl());
    }

    public ResponseEntity<byte[]> forwardToMechanicService(HttpServletRequest request, byte[] requestBody) {
        return forward(request, requestBody, dispatcherProperties.mechanicServiceUrl());
    }

    public ResponseEntity<byte[]> forwardToUserService(HttpServletRequest request, byte[] requestBody) {
        return forward(request, requestBody, dispatcherProperties.userServiceUrl());
    }

    public ResponseEntity<byte[]> forwardToPaymentService(HttpServletRequest request, byte[] requestBody) {
        return forward(request, requestBody, dispatcherProperties.paymentServiceUrl());
    }

    public ResponseEntity<byte[]> forwardToNotificationService(HttpServletRequest request, byte[] requestBody) {
        return forward(request, requestBody, dispatcherProperties.notificationServiceUrl());
    }

    private ResponseEntity<byte[]> forward(HttpServletRequest request, byte[] requestBody, String baseUrl) {
        try {
            return restClient.method(HttpMethod.valueOf(request.getMethod()))
                    .uri(buildTargetUrl(request, baseUrl))
                    .headers(headers -> copyHeaders(request, headers))
                    .contentType(resolveContentType(request))
                    .body(requestBody == null ? new byte[0] : requestBody)
                    .retrieve()
                    .toEntity(byte[].class);
        } catch (ResourceAccessException exception) {
            throw new ServiceUnavailableException("Target service is unavailable", exception);
        }
    }

    private String buildTargetUrl(HttpServletRequest request, String baseUrl) {
        String requestPath = request.getRequestURI().substring(API_PREFIX.length());
        String query = request.getQueryString();

        if (!StringUtils.hasText(query)) {
            return baseUrl + requestPath;
        }

        return baseUrl + requestPath + "?" + query;
    }

    private void copyHeaders(HttpServletRequest request, HttpHeaders headers) {
        Collections.list(request.getHeaderNames())
                .forEach(headerName -> headers.put(headerName, Collections.list(request.getHeaders(headerName))));
        headers.remove(HttpHeaders.HOST);
        headers.remove(HttpHeaders.AUTHORIZATION);
        headers.set(dispatcherProperties.internalHeaderName(), dispatcherProperties.internalSharedSecret());
        copyAuthenticatedUserHeaders(request, headers);
    }

    private void copyAuthenticatedUserHeaders(HttpServletRequest request, HttpHeaders headers) {
        Object userId = request.getAttribute(DispatcherAuthorizationFilter.AUTHENTICATED_USER_ID);
        Object userRole = request.getAttribute(DispatcherAuthorizationFilter.AUTHENTICATED_USER_ROLE);

        if (userId != null) {
            headers.set(USER_ID_HEADER, userId.toString());
        }

        if (userRole != null) {
            headers.set(USER_ROLE_HEADER, userRole.toString());
        }
    }

    private MediaType resolveContentType(HttpServletRequest request) {
        if (!StringUtils.hasText(request.getContentType())) {
            return MediaType.APPLICATION_JSON;
        }

        return MediaType.parseMediaType(request.getContentType());
    }
}

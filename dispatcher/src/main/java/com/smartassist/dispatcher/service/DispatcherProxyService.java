package com.smartassist.dispatcher.service;

import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import com.smartassist.dispatcher.config.DispatcherProperties;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DispatcherProxyService {

    private static final String API_PREFIX = "/api";

    private final RestClient restClient;
    private final DispatcherProperties dispatcherProperties;

    public ResponseEntity<byte[]> forwardToRequestService(HttpServletRequest request, byte[] requestBody) {
        return restClient.method(HttpMethod.valueOf(request.getMethod()))
                .uri(buildTargetUrl(request))
                .headers(headers -> copyHeaders(request, headers))
                .contentType(resolveContentType(request))
                .body(requestBody == null ? new byte[0] : requestBody)
                .retrieve()
                .toEntity(byte[].class);
    }

    private String buildTargetUrl(HttpServletRequest request) {
        String requestPath = request.getRequestURI().substring(API_PREFIX.length());
        String query = request.getQueryString();
        String baseUrl = dispatcherProperties.requestServiceUrl();

        if (!StringUtils.hasText(query)) {
            return baseUrl + requestPath;
        }

        return baseUrl + requestPath + "?" + query;
    }

    private void copyHeaders(HttpServletRequest request, HttpHeaders headers) {
        Collections.list(request.getHeaderNames())
                .forEach(headerName -> headers.put(headerName, Collections.list(request.getHeaders(headerName))));
        headers.remove(HttpHeaders.HOST);
    }

    private MediaType resolveContentType(HttpServletRequest request) {
        if (!StringUtils.hasText(request.getContentType())) {
            return MediaType.APPLICATION_JSON;
        }

        return MediaType.parseMediaType(request.getContentType());
    }
}

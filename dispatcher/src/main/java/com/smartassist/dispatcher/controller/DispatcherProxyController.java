package com.smartassist.dispatcher.controller;

import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.smartassist.dispatcher.config.DispatcherProperties;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DispatcherProxyController {

    private static final String API_PREFIX = "/api";

    private final RestClient restClient;
    private final DispatcherProperties dispatcherProperties;

    @RequestMapping({"/api/requests", "/api/requests/**"})
    public ResponseEntity<byte[]> proxyRequestService(HttpServletRequest request,
                                                      @RequestBody(required = false) byte[] requestBody) {
        String targetUrl = buildTargetUrl(request);

        RestClient.ResponseSpec responseSpec = restClient.method(HttpMethod.valueOf(request.getMethod()))
                .uri(targetUrl)
                .headers(headers -> {
                    Collections.list(request.getHeaderNames())
                            .forEach(headerName -> headers.put(headerName, Collections.list(request.getHeaders(headerName))));
                    headers.remove(HttpHeaders.HOST);
                })
                .contentType(resolveContentType(request))
                .body(requestBody == null ? new byte[0] : requestBody)
                .retrieve();

        return responseSpec.toEntity(byte[].class);
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

    private MediaType resolveContentType(HttpServletRequest request) {
        if (!StringUtils.hasText(request.getContentType())) {
            return MediaType.APPLICATION_JSON;
        }

        return MediaType.parseMediaType(request.getContentType());
    }
}

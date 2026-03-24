package com.smartassist.dispatcher.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.smartassist.dispatcher.service.DispatcherAuthorizationService;
import com.smartassist.dispatcher.service.DispatcherServiceResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DispatcherAuthorizationFilter extends OncePerRequestFilter {

    private final DispatcherProperties dispatcherProperties;
    private final DispatcherAuthorizationService dispatcherAuthorizationService;
    private final DispatcherServiceResolver dispatcherServiceResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!dispatcherProperties.authEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String serviceName = dispatcherServiceResolver.resolveServiceName(request.getRequestURI());
        if (!StringUtils.hasText(serviceName)) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(dispatcherProperties.apiKeyHeaderName());
        if (!StringUtils.hasText(apiKey)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing API key");
            return;
        }

        if (!dispatcherAuthorizationService.isAuthorized(apiKey, serviceName)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Forbidden");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

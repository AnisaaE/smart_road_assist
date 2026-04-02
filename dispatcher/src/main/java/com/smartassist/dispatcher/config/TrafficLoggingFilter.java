package com.smartassist.dispatcher.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TrafficLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrafficLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long startedAt = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = System.currentTimeMillis() - startedAt;
            Object userId = request.getAttribute(DispatcherAuthorizationFilter.AUTHENTICATED_USER_ID);
            Object userRole = request.getAttribute(DispatcherAuthorizationFilter.AUTHENTICATED_USER_ROLE);
            LOGGER.info("dispatcher traffic method={} path={} userId={} role={} status={} durationMs={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    userId,
                    userRole,
                    response.getStatus(),
                    durationMs);
        }
    }
}

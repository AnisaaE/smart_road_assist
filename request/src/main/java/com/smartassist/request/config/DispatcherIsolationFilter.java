package com.smartassist.request.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@ConditionalOnProperty(name = "dispatcher.enforce-internal-header", havingValue = "true", matchIfMissing = true)
public class DispatcherIsolationFilter extends OncePerRequestFilter {

    private final String internalHeaderName;
    private final String sharedSecret;

    public DispatcherIsolationFilter(@Value("${dispatcher.internal-header-name}") String internalHeaderName,
                                     @Value("${dispatcher.shared-secret}") String sharedSecret) {
        this.internalHeaderName = internalHeaderName;
        this.sharedSecret = sharedSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String providedSecret = request.getHeader(internalHeaderName);
        if (!sharedSecret.equals(providedSecret)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Dispatcher access required");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

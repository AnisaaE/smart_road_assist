package com.smartassist.dispatcher.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.smartassist.dispatcher.service.DispatcherAccessPolicyService;
import com.smartassist.dispatcher.service.DispatcherServiceResolver;
import com.smartassist.dispatcher.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DispatcherAuthorizationFilter extends OncePerRequestFilter {

    public static final String AUTHENTICATED_USER_ID = "authenticatedUserId";
    public static final String AUTHENTICATED_USER_ROLE = "authenticatedUserRole";

    private final DispatcherProperties dispatcherProperties;
    private final DispatcherServiceResolver dispatcherServiceResolver;
    private final JwtService jwtService;
    private final DispatcherAccessPolicyService dispatcherAccessPolicyService;

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

        String authorizationHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing bearer token");
            return;
        }

        String token = authorizationHeader.substring(7);
        var authenticatedUser = jwtService.parseToken(token)
                .orElse(null);

        if (authenticatedUser == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token");
            return;
        }

        if (!dispatcherAccessPolicyService.isAllowed(authenticatedUser, request, serviceName)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Forbidden");
            return;
        }

        request.setAttribute(AUTHENTICATED_USER_ID, authenticatedUser.userId());
        request.setAttribute(AUTHENTICATED_USER_ROLE, authenticatedUser.role());
        filterChain.doFilter(request, response);
    }
}

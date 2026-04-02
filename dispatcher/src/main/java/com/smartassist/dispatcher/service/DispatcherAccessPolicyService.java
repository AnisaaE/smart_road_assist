package com.smartassist.dispatcher.service;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.smartassist.dispatcher.model.AuthenticatedUser;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class DispatcherAccessPolicyService {

    public boolean isAllowed(AuthenticatedUser user, HttpServletRequest request, String serviceName) {
        if ("mechanics".equals(serviceName)) {
            return hasAnyRole(user, "ADMIN", "MECHANIC");
        }

        if ("users".equals(serviceName)) {
            return isUserRouteAllowed(user, request);
        }

        return true;
    }

    private boolean isUserRouteAllowed(AuthenticatedUser user, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();

        if (HttpMethod.DELETE.matches(method) || (HttpMethod.PUT.matches(method) && requestUri.endsWith("/role"))) {
            return hasAnyRole(user, "ADMIN");
        }

        if (HttpMethod.GET.matches(method)) {
            String requestedUserId = extractUserId(requestUri);
            return hasAnyRole(user, "ADMIN") || requestedUserId.equals(user.userId());
        }

        return hasAnyRole(user, "ADMIN");
    }

    private String extractUserId(String requestUri) {
        String[] pathParts = requestUri.split("/");
        return pathParts[pathParts.length - 1];
    }

    private boolean hasAnyRole(AuthenticatedUser user, String... roles) {
        for (String role : roles) {
            if (role.equals(user.role())) {
                return true;
            }
        }
        return false;
    }
}

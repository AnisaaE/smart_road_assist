package com.smartassist.dispatcher.service;

import org.springframework.stereotype.Service;

@Service
public class DispatcherServiceResolver {

    public String resolveServiceName(String requestUri) {
        if (requestUri.startsWith("/api/requests")) {
            return "requests";
        }

        if (requestUri.startsWith("/api/mechanics")) {
            return "mechanics";
        }

        if (requestUri.startsWith("/api/users")) {
            return "users";
        }

        if (requestUri.startsWith("/api/payments")) {
            return "payments";
        }

        if (requestUri.startsWith("/api/notifications")) {
            return "notifications";
        }

        return null;
    }
}

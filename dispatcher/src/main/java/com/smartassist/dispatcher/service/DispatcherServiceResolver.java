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

        return null;
    }
}

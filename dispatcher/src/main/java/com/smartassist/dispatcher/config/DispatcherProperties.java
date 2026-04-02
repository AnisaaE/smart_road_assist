package com.smartassist.dispatcher.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dispatcher")
public record DispatcherProperties(
        String requestServiceUrl,
        String mechanicServiceUrl,
        String userServiceUrl,
        String paymentServiceUrl,
        String notificationServiceUrl,
        boolean authEnabled,
        String apiKeyHeaderName,
        String internalHeaderName,
        String internalSharedSecret,
        String jwtSecret
) {
}

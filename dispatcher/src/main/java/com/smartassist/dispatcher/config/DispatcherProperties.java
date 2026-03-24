package com.smartassist.dispatcher.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dispatcher")
public record DispatcherProperties(String requestServiceUrl) {
}

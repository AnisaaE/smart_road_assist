package com.smartassist.dispatcher.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.smartassist.dispatcher.model.ApiClient;
import com.smartassist.dispatcher.repository.ApiClientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DispatcherAuthorizationService {

    private static final String GLOBAL_ACCESS = "*";

    private final ApiClientRepository apiClientRepository;

    public boolean isAuthorized(String apiKey, String serviceName) {
        if (!StringUtils.hasText(apiKey) || !StringUtils.hasText(serviceName)) {
            return false;
        }

        return apiClientRepository.findByApiKey(apiKey)
                .filter(ApiClient::active)
                .map(ApiClient::allowedServices)
                .filter(allowedServices -> hasAccess(allowedServices, serviceName))
                .isPresent();
    }

    private boolean hasAccess(Set<String> allowedServices, String serviceName) {
        return allowedServices != null
                && (allowedServices.contains(GLOBAL_ACCESS) || allowedServices.contains(serviceName));
    }
}

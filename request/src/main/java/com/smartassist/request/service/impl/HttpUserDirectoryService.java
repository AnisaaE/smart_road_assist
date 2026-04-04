package com.smartassist.request.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.smartassist.request.service.UserDirectoryService;

@Service
public class HttpUserDirectoryService implements UserDirectoryService {

    private final RestClient restClient;
    private final String userServiceBaseUrl;

    public HttpUserDirectoryService(RestClient.Builder restClientBuilder,
                                    @Value("${services.user.base-url:http://localhost:8083}") String userServiceBaseUrl) {
        this.restClient = restClientBuilder.build();
        this.userServiceBaseUrl = userServiceBaseUrl;
    }

    @Override
    public boolean userExists(String userId) {
        try {
            restClient.get()
                    .uri(userServiceBaseUrl + "/users/" + userId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new RestClientResponseException(
                                "User lookup failed with client error",
                                response.getStatusCode().value(),
                                response.getStatusText(),
                                response.getHeaders(),
                                null,
                                null);
                    })
                    .toBodilessEntity();
            return true;
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                return false;
            }
            throw ex;
        }
    }
}

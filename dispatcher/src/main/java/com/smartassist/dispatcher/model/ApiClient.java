package com.smartassist.dispatcher.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "api_clients")
public record ApiClient(
        @Id String id,
        String apiKey,
        boolean active,
        Set<String> allowedServices
) {
}

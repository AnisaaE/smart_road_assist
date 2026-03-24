package com.smartassist.dispatcher.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartassist.dispatcher.model.ApiClient;

public interface ApiClientRepository extends MongoRepository<ApiClient, String> {

    Optional<ApiClient> findByApiKey(String apiKey);
}

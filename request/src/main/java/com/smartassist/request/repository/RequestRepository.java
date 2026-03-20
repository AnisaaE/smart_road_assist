package com.smartassist.request.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartassist.request.model.AssistanceRequest;

public interface RequestRepository extends MongoRepository<AssistanceRequest, String> {
}

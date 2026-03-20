package com.smartassist.mechanic.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartassist.mechanic.model.MechanicProfile;

public interface MechanicRepository extends MongoRepository<MechanicProfile, String> {
}

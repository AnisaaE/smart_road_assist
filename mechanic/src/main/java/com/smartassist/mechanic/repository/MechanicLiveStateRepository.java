package com.smartassist.mechanic.repository;

import org.springframework.data.repository.CrudRepository;

import com.smartassist.mechanic.model.MechanicLiveState;

public interface MechanicLiveStateRepository extends CrudRepository<MechanicLiveState, String> {
}

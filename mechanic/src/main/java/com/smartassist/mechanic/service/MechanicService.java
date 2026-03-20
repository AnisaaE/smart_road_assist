package com.smartassist.mechanic.service;

import java.util.List;

import com.smartassist.mechanic.dto.request.CreateMechanicRequest;
import com.smartassist.mechanic.dto.response.MechanicResponse;

public interface MechanicService {

    MechanicResponse createMechanic(CreateMechanicRequest request);

    List<MechanicResponse> getAllMechanics();
}

package com.smartassist.mechanic.service;

import java.util.List;

import com.smartassist.mechanic.dto.request.CreateMechanicRequest;
import com.smartassist.mechanic.dto.request.UpdateMechanicStatusRequest;
import com.smartassist.mechanic.dto.request.UpdateMechanicRequest;
import com.smartassist.mechanic.dto.response.MechanicResponse;
import com.smartassist.mechanic.dto.response.MechanicStateResponse;

public interface MechanicService {

    MechanicResponse createMechanic(CreateMechanicRequest request);

    List<MechanicResponse> getAllMechanics();

    MechanicResponse getMechanicById(String id);

    MechanicResponse updateMechanic(String id, UpdateMechanicRequest request);

    void deleteMechanic(String id);

    MechanicStateResponse updateMechanicStatus(String id, UpdateMechanicStatusRequest request);
}

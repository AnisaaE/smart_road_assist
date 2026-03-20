package com.smartassist.mechanic.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.smartassist.mechanic.dto.request.CreateMechanicRequest;
import com.smartassist.mechanic.dto.request.UpdateMechanicRequest;
import com.smartassist.mechanic.dto.response.MechanicResponse;
import com.smartassist.mechanic.exception.MechanicNotFoundException;
import com.smartassist.mechanic.mapper.MechanicMapper;
import com.smartassist.mechanic.model.MechanicProfile;
import com.smartassist.mechanic.repository.MechanicRepository;
import com.smartassist.mechanic.service.MechanicService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MechanicServiceImpl implements MechanicService {

    private final MechanicRepository mechanicRepository;
    private final MechanicMapper mechanicMapper;

    @Override
    public MechanicResponse createMechanic(CreateMechanicRequest request) {
        MechanicProfile mechanicProfile = mechanicMapper.toEntity(request, Instant.now());
        MechanicProfile savedProfile = mechanicRepository.save(mechanicProfile);
        return mechanicMapper.toResponse(savedProfile);
    }

    @Override
    public List<MechanicResponse> getAllMechanics() {
        return mechanicMapper.toResponseList(mechanicRepository.findAll());
    }

    @Override
    public MechanicResponse getMechanicById(String id) {
        return mechanicMapper.toResponse(findMechanicById(id));
    }

    @Override
    public MechanicResponse updateMechanic(String id, UpdateMechanicRequest request) {
        MechanicProfile mechanicProfile = findMechanicById(id);
        mechanicMapper.applyUpdate(mechanicProfile, request);
        return mechanicMapper.toResponse(mechanicRepository.save(mechanicProfile));
    }

    private MechanicProfile findMechanicById(String id) {
        return mechanicRepository.findById(id)
                .orElseThrow(() -> new MechanicNotFoundException(buildMechanicNotFoundMessage(id)));
    }

    private String buildMechanicNotFoundMessage(String id) {
        return "Mechanic not found: " + id;
    }
}

package com.smartassist.mechanic.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.smartassist.mechanic.dto.request.CreateMechanicRequest;
import com.smartassist.mechanic.dto.response.MechanicResponse;
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
}

package com.smartassist.mechanic.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smartassist.mechanic.dto.request.CreateMechanicRequest;
import com.smartassist.mechanic.dto.request.UpdateMechanicRequest;
import com.smartassist.mechanic.dto.response.MechanicResponse;
import com.smartassist.mechanic.service.MechanicService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(MechanicController.MECHANICS_PATH)
@RequiredArgsConstructor
public class MechanicController {

    static final String MECHANICS_PATH = "/mechanics";
    private static final String MECHANIC_ID_PATH = "/{id}";

    private final MechanicService mechanicService;

    @GetMapping
    public List<MechanicResponse> getAllMechanics() {
        return mechanicService.getAllMechanics();
    }

    @GetMapping(MECHANIC_ID_PATH)
    public MechanicResponse getMechanicById(@PathVariable String id) {
        return mechanicService.getMechanicById(id);
    }

    @PutMapping(MECHANIC_ID_PATH)
    public MechanicResponse updateMechanic(@PathVariable String id, @RequestBody UpdateMechanicRequest request) {
        return mechanicService.updateMechanic(id, request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MechanicResponse createMechanic(@Valid @RequestBody CreateMechanicRequest request) {
        return mechanicService.createMechanic(request);
    }
}

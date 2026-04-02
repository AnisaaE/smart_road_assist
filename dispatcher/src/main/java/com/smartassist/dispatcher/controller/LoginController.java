package com.smartassist.dispatcher.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartassist.dispatcher.dto.request.LoginRequest;
import com.smartassist.dispatcher.dto.request.RegistrationRequest;
import com.smartassist.dispatcher.dto.response.LoginResponse;
import com.smartassist.dispatcher.dto.response.RegistrationResponse;
import com.smartassist.dispatcher.service.AuthenticationService;
import com.smartassist.dispatcher.service.RegistrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationService authenticationService;
    private final RegistrationService registrationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.status(201).body(registrationService.register(request));
    }
}

package com.smartassist.dispatcher.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartassist.dispatcher.dto.request.LoginRequest;
import com.smartassist.dispatcher.dto.response.LoginResponse;
import com.smartassist.dispatcher.exception.InvalidCredentialsException;
import com.smartassist.dispatcher.model.AuthUser;
import com.smartassist.dispatcher.repository.AuthUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        AuthUser authUser = authUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), authUser.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return new LoginResponse(
                jwtService.generateToken(authUser.getEmail(), authUser.getUserId(), authUser.getRole()),
                authUser.getUserId(),
                authUser.getRole()
        );
    }
}

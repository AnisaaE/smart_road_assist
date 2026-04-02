package com.smartassist.dispatcher.service;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.smartassist.dispatcher.config.DispatcherProperties;
import com.smartassist.dispatcher.dto.request.RegistrationRequest;
import com.smartassist.dispatcher.dto.response.RegistrationResponse;
import com.smartassist.dispatcher.model.AuthUser;
import com.smartassist.dispatcher.repository.AuthUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RestClient restClient;
    private final DispatcherProperties dispatcherProperties;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public RegistrationResponse register(RegistrationRequest request) {
        String role = request.role() == null || request.role().isBlank() ? "USER" : request.role();

        UserRegistrationResponse userResponse = restClient.post()
                .uri(dispatcherProperties.userServiceUrl() + "/users")
                .header(dispatcherProperties.internalHeaderName(), dispatcherProperties.internalSharedSecret())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "name", request.name(),
                        "email", request.email(),
                        "phone", request.phone(),
                        "role", role
                ))
                .retrieve()
                .body(UserRegistrationResponse.class);

        AuthUser authUser = AuthUser.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(role)
                .userId(userResponse.id())
                .build();

        authUserRepository.save(authUser);

        return new RegistrationResponse(
                jwtService.generateToken(request.email(), userResponse.id(), role),
                userResponse.id(),
                role
        );
    }

    private record UserRegistrationResponse(String id) {
    }
}

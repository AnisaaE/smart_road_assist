package com.smartassist.dispatcher.dto.request;

public record RegistrationRequest(
        String name,
        String email,
        String phone,
        String password,
        String role
) {
}

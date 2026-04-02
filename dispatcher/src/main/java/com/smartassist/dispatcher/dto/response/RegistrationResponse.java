package com.smartassist.dispatcher.dto.response;

public record RegistrationResponse(
        String token,
        String userId,
        String role
) {
}

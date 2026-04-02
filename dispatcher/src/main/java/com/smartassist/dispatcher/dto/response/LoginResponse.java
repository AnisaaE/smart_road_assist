package com.smartassist.dispatcher.dto.response;

public record LoginResponse(
        String token,
        String userId,
        String role
) {
}

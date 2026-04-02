package com.smartassist.dispatcher.model;

public record AuthenticatedUser(
        String email,
        String userId,
        String role
) {
}

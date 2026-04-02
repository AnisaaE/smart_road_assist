package com.smartassist.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartassist.user.dto.UserResponseDTO;
import com.smartassist.user.model.User;
import com.smartassist.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceUpdateRoleTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldUpdateUserRole() {
        User existingUser = User.builder()
                .id("user-1")
                .name("Alice")
                .email("alice@smartassist.com")
                .phone("555")
                .role("USER")
                .status("ACTIVE")
                .build();

        when(userRepository.findById("user-1")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO response = userService.updateRole("user-1", "ADMIN");

        assertEquals("ADMIN", response.getRole());
    }
}

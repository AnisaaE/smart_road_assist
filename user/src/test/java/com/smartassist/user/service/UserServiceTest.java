package com.smartassist.user.service;

import com.smartassist.user.dto.UserResponseDTO;
import com.smartassist.user.exception.InvalidStatusException;
import com.smartassist.user.model.User;
import com.smartassist.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // Spring context yüklemeden hızlıca test etmek için
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldThrowExceptionWhenStatusIsInvalid() {
        String userId = "1";
        String invalidStatus = "MAVI_EKRAN";

        // GIVEN: Kullanıcı veri tabanında var
        User mockUser = User.builder().id(userId).status("ACTIVE").build();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // WHEN & THEN: Geçersiz status fırlatmalı (GREEN beklentisi)
        assertThrows(InvalidStatusException.class, () -> {
            userService.updateStatus(userId, invalidStatus);
        });
    }

    @Test
    public void shouldUpdateStatusSuccessfully() {
        String userId = "1";
        String newStatus = "BUSY";

        // GIVEN
        User mockUser = User.builder().id(userId).status("ACTIVE").build();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(mockUser);

        // WHEN
        UserResponseDTO result = userService.updateStatus(userId, newStatus);

        // THEN
        assertNotNull(result);
        assertEquals("BUSY", mockUser.getStatus()); // Entity güncellendi mi?
    }
}
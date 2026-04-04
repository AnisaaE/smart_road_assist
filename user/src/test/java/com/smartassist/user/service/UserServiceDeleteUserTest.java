package com.smartassist.user.service;

import com.smartassist.user.exception.UserNotFoundException;
import com.smartassist.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceDeleteUserTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void whenUserExists_shouldDeleteSuccessfully() {
        String userId = "user-123";
        
        // GIVEN: Kullanıcı veritabanında var
        when(userRepository.existsById(userId)).thenReturn(true);

        // WHEN: Silme işlemi çağrılıyor
        userService.deleteUser(userId);

        // THEN: Repository'nin silme metodu tam 1 kez çağrılmalı
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        String userId = "non-existent-id";

        // GIVEN: Kullanıcı veritabanında YOK
        when(userRepository.existsById(userId)).thenReturn(false);

        // WHEN & THEN: Hata fırlatmasını bekliyoruz
        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(userId);
        });
        
        // Repository'nin silme metodu ASLA çağrılmamalı
        verify(userRepository, never()).deleteById(anyString());
    }
}
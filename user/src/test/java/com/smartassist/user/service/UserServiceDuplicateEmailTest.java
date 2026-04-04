package com.smartassist.user.service;

import com.smartassist.user.dto.UserRequestDTO;
import com.smartassist.user.exception.UserAlreadyExistsException; // Bu exception'ı oluşturman gerekebilir
import com.smartassist.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceDuplicateEmailTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void whenEmailAlreadyExists_shouldThrowUserAlreadyExistsException() {
        // GIVEN
        String existingEmail = "ulku@test.com";
        UserRequestDTO request = new UserRequestDTO();
        request.setName("Ülkü");
        request.setEmail(existingEmail);

        // Repository'de bu email'in zaten olduğunu simüle ediyoruz
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        // WHEN & THEN
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(request);
        });

        // Hata fırladığı için kaydetme (save) metodu asla çağrılmamalı
        verify(userRepository, never()).save(any());
    }
}
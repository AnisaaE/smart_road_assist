package com.smartassist.user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserServiceTest {
    @Test
public void shouldThrowExceptionWhenStatusIsInvalid() {
    String userId = "1";
    String invalidStatus = "MAVI_EKRAN"; // Geçersiz status

    // GIVEN: Kullanıcı var
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

    // WHEN & THEN: InvalidStatusException fırlatılmasını bekliyoruz
    assertThrows(InvalidStatusException.class, () -> {
        userService.updateStatus(userId, invalidStatus);
    });
}

}

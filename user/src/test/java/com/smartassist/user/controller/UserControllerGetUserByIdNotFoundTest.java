package com.smartassist.user.controller;

import com.smartassist.user.service.UserService;
import com.smartassist.user.exception.UserNotFoundException; // Bu sınıf henüz yok, hata verecek!
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerGetUserByIdNotFoundTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    public void shouldReturn404WhenUserNotFound() throws Exception {
        // GIVEN: Servis katmanı UserNotFoundException fırlatacak şekilde ayarlanır
        String nonExistentId = "999";
        Mockito.when(userService.getUserById(nonExistentId))
               .thenThrow(new UserNotFoundException("User not found"));

        // WHEN & THEN: 404 Not Found bekliyoruz
        mockMvc.perform(get("/users/" + nonExistentId))
                .andExpect(status().isNotFound()); // RMM Level 2: 404 Kontrolü [cite: 59]
    }
}
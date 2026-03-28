package com.smartassist.user.controller;

import com.smartassist.user.model.User; // User modelini tanıması için
import com.smartassist.user.service.UserService; // Service interface'ini tanıması için
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerGetUserByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnUserWhenIdExists() throws Exception {
        // GIVEN: Hazırlık
        User mockUser = new User("1", "ulku", "5551234567", "Kocaeli");
        Mockito.when(userService.getUserById("1")).thenReturn(mockUser);

        // WHEN & THEN: Çalıştırma ve Doğrulama
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk()) // 200 OK bekliyoruz
                .andExpect(jsonPath("$.username").value("ulku"))
                .andExpect(jsonPath("$.phone").value("5551234567"));
    }
}
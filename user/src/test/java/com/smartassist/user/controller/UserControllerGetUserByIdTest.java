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
        // GIVEN: Modeldeki alan isimlerine (name) göre nesne oluşturuyoruz
        User mockUser = new User("1", "ulku", "ulku@mail.com", "USER");
        Mockito.when(userService.getUserById("1")).thenReturn(mockUser);

        // WHEN & THEN
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ulku")) // $.username değil, $.name olmalı
                .andExpect(jsonPath("$.email").value("ulku@mail.com")); // Modelde phone yok, email var
    }
}
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
        // 5 Parametre: id, name, email, phone, role [cite: 62]
        User mockUser = User.builder()
    .id("1")
    .name("ulku")
    .email("ulku@mail.com")
    .status("ACTIVE") // Sadece istediğin alanları set edebilirsin
    .build();
        Mockito.when(userService.getUserById("1")).thenReturn(mockUser);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ulku"))
                .andExpect(jsonPath("$.phone").value("5551234567")) // Yeni eklediğimiz phone alanını da test edelim
                .andExpect(jsonPath("$.email").value("ulku@mail.com"));
    }
}
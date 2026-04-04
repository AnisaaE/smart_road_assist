package com.smartassist.user.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.smartassist.user.dto.UserResponseDTO;
import com.smartassist.user.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerGetAllUsersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getAllUsersShouldReturnUserList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(
                UserResponseDTO.builder()
                        .id("user-1")
                        .name("Alice")
                        .email("alice@example.com")
                        .phone("111")
                        .role("USER")
                        .status("ACTIVE")
                        .build(),
                UserResponseDTO.builder()
                        .id("user-2")
                        .name("Bob")
                        .email("bob@example.com")
                        .phone("222")
                        .role("ADMIN")
                        .status("ACTIVE")
                        .build()
        ));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("user-1"))
                .andExpect(jsonPath("$[1].id").value("user-2"));
    }
}

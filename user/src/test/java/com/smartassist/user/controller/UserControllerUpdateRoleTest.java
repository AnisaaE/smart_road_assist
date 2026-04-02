package com.smartassist.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartassist.user.dto.UserResponseDTO;
import com.smartassist.user.service.IUserService;

@WebMvcTest(UserController.class)
class UserControllerUpdateRoleTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IUserService userService;

    @Test
    void shouldUpdateUserRole() throws Exception {
        UserResponseDTO responseDto = UserResponseDTO.builder()
                .id("user-1")
                .name("Alice")
                .email("alice@smartassist.com")
                .phone("555")
                .role("ADMIN")
                .status("ACTIVE")
                ._links(Map.of("self", "/users/user-1"))
                .build();

        Mockito.when(userService.updateRole("user-1", "ADMIN"))
                .thenReturn(responseDto);

        mockMvc.perform(put("/users/user-1/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("role", "ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }
}

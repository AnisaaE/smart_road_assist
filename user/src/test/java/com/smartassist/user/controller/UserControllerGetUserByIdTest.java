package com.smartassist.user.controller;

import com.smartassist.user.dto.UserResponseDTO; // DTO import edildi
import com.smartassist.user.service.IUserService; // Interface import edildi
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // Yeni standart
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerGetUserByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // UserService yerine IUserService mock'lıyoruz
    private IUserService userService;

    @Test
    public void shouldReturnUserDtoWhenIdExists() throws Exception {
        String userId = "1";
        
        // GIVEN: UserResponseDTO hazırlıyoruz (Entity değil!)
        UserResponseDTO mockResponse = UserResponseDTO.builder()
                .id(userId)
                .name("ulku")
                .email("ulku@mail.com")
                .phone("5551234567")
                .role("USER")
                .status("ACTIVE")
                ._links(Map.of("self", "/users/" + userId)) // HATEOAS Linki
                .build();

        Mockito.when(userService.getUserById(userId)).thenReturn(mockResponse);

        // WHEN & THEN
        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ulku"))
                .andExpect(jsonPath("$.email").value("ulku@mail.com"))
                .andExpect(jsonPath("$.phone").value("5551234567"))
                .andExpect(jsonPath("$._links.self").value("/users/1")); // Link doğrulaması
    }
}
package com.smartassist.user.controller;

import com.smartassist.user.dto.UserRequestDTO;
import com.smartassist.user.dto.UserResponseDTO;
import com.smartassist.user.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerRefactorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IUserService userService; // HATA: IUserService henüz yok!

    @Test
    public void shouldReturnUserWithHateoasLinks() throws Exception {
        String userId = "1";
        
        // GIVEN: Beklenen HATEOAS yapılı Response DTO (HATA: UserResponseDTO henüz yok!)
        UserResponseDTO response = UserResponseDTO.builder()
                .id(userId)
                .name("ulku")
                .email("ulku@mail.com")
                .status("ACTIVE")
                ._links(Map.of(
                    "self", "/users/" + userId,
                    "update", "/users/" + userId,
                    "delete", "/users/" + userId
                ))
                .build();

        Mockito.when(userService.getUserById(userId)).thenReturn(response);

        // WHEN & THEN
        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ulku"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$._links.self").value("/users/1"))
                .andExpect(jsonPath("$._links.update").exists());
    }

    @Test
    public void shouldCreateUserUsingRequestDto() throws Exception {
        // GIVEN: Request DTO (HATA: UserRequestDTO henüz yok!)
        UserRequestDTO request = UserRequestDTO.builder()
                .name("ulku")
                .email("ulku@mail.com")
                .phone("5551234")
                .role("USER")
                .build();

        UserResponseDTO response = UserResponseDTO.builder()
                .id("generated-id")
                .name("ulku")
                .status("ACTIVE")
                .build();

        Mockito.when(userService.createUser(Mockito.any(UserRequestDTO.class))).thenReturn(response);

        // WHEN & THEN
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}
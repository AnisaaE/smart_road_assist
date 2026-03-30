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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerUpdateUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean // Somut UserService yerine IUserService mock'lıyoruz
    private IUserService userService;

    @Test
    public void shouldUpdateUserSuccessfully() throws Exception {
        String userId = "1";

        // 1. GIVEN: İstek (Request) DTO hazırlıyoruz
        UserRequestDTO requestDto = UserRequestDTO.builder()
                .name("ulku_yeni")
                .email("yeni@mail.com")
                .phone("5559999")
                .role("USER")
                .build();

        // 2. GIVEN: Servis'ten beklediğimiz Yanıt (Response) DTO
        UserResponseDTO responseDto = UserResponseDTO.builder()
                .id(userId)
                .name("ulku_yeni")
                .email("yeni@mail.com")
                .phone("5559999")
                .status("ACTIVE")
                ._links(Map.of("self", "/users/" + userId))
                .build();

        // Mock: updateUser(String, UserRequestDTO) çağrıldığında responseDto dön
        Mockito.when(userService.updateUser(Mockito.eq(userId), Mockito.any(UserRequestDTO.class)))
               .thenReturn(responseDto);

        // 3. WHEN & THEN: PUT isteği atıyoruz
        mockMvc.perform(put("/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))) // JSON olarak DTO gönderiyoruz
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ulku_yeni"))
                .andExpect(jsonPath("$.phone").value("5559999"))
                .andExpect(jsonPath("$._links.self").exists());
    }
}

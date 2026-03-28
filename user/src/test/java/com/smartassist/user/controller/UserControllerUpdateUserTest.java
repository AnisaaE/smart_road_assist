package com.smartassist.user.controller;

import com.smartassist.user.model.User;
import com.smartassist.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerUpdateUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Nesneyi JSON'a çevirmek için

    @MockitoBean
    private UserService userService;

    @Test
    public void shouldUpdateUserSuccessfully() throws Exception {
        String userId = "1";
        // Güncellenecek yeni veriler
        User updatedInfo = new User(userId, "ulku_yeni", "yeni@mail.com", "5559999", "USER");
        
        Mockito.when(userService.updateUser(Mockito.eq(userId), Mockito.any(User.class)))
               .thenReturn(updatedInfo);

        // WHEN & THEN: PUT isteği atıyoruz
        mockMvc.perform(put("/users/" + userId)
                .contentType(MediaType.APPLICATION_BITSTREAM.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ulku_yeni"))
                .andExpect(jsonPath("$.phone").value("5559999"));
    }
}
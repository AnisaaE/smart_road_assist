package com.smartassist.user.controller;
//model içindeki user ı boş bıraktık çünkü test için gerekli değil, sadece controller katmanını test edeceğiz.
import com.smartassist.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class) // Sadece Controller katmanını ayağa kaldırır
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturn201WhenUserIsCreated() throws Exception {
        String userJson = "{\"name\":\"Beyza\",\"email\":\"beyza@example.com\",\"role\":\"USER\"}";

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated()); // 201 bekliyoruz
    }
}
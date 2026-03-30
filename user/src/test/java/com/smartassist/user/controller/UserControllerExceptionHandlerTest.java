package com.smartassist.user.controller;

import com.smartassist.user.exception.UserNotFoundException;
import com.smartassist.user.service.IUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IUserService userService;

    @Test
    public void shouldReturn404WhenUserNotFound() throws Exception {
        String userId = "999";

        // GIVEN: Servis metodunun hata fırlatacağını simüle ediyoruz
        Mockito.when(userService.getUserById(userId))
               .thenThrow(new UserNotFoundException("User not found: " + userId));

        // WHEN & THEN: 404 durumu ve mesaj içeriği kontrol ediliyor
        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isNotFound()) // 404 Beklentisi
                .andExpect(jsonPath("$.message").value("User not found: 999"))
                .andExpect(jsonPath("$.status").value(404));
    }
}
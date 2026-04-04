package com.smartassist.user.controller;

import com.smartassist.user.exception.GlobalExceptionHandler;
import com.smartassist.user.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// Statik importlar: Kırmızı yanan yerleri bunlar çözer
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
public class UserControllerValidationTest {

    @Autowired 
    private MockMvc mockMvc;

    @MockBean 
    private IUserService userService;

    @Test
    void whenEmailIsInvalid_thenReturns400() throws Exception {
        // DTO'da "name" yazdığı için burayı "name" yaptık. 
        // "email" formatı ise kasten hatalı ("hatali-email").
        String invalidUser = "{\"name\": \"ulku\", \"email\": \"hatali-email\", \"phone\": \"12345\"}";
        
        // Controller'daki @RequestMapping("/users") ile burası aynı olmalı.
        // Eğer Controller'da "/api/v1/users" yapmadıysan burayı "/users" yap.
        mockMvc.perform(post("/users") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidUser))
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }
}
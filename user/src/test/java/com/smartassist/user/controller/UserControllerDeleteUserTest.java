package com.smartassist.user.controller;

import com.smartassist.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerDeleteUserTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    public void shouldReturn204WhenUserDeletedSuccessfully() throws Exception {
        String userId = "1";
        
        // Mock: service.deleteUser çağrıldığında hiçbir şey yapma (void metot)
        Mockito.doNothing().when(userService).deleteUser(userId);

        // WHEN & THEN: delete isteği atıyoruz ve 204 bekliyoruz
        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isNoContent()); // RMM Level 2: 204 No Content [cite: 59]
                
        // Doğrulama: Servis katmanı gerçekten çağrıldı mı?
        Mockito.verify(userService, Mockito.times(1)).deleteUser(userId);
    }
}
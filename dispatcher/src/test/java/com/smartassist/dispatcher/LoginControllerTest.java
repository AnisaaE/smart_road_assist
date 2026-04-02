package com.smartassist.dispatcher;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.smartassist.dispatcher.config.DispatcherProperties;
import com.smartassist.dispatcher.config.PasswordConfig;
import com.smartassist.dispatcher.config.SecurityConfig;
import com.smartassist.dispatcher.controller.LoginController;
import com.smartassist.dispatcher.model.AuthUser;
import com.smartassist.dispatcher.repository.AuthUserRepository;
import com.smartassist.dispatcher.service.AuthenticationService;
import com.smartassist.dispatcher.service.DispatcherAccessPolicyService;
import com.smartassist.dispatcher.service.DispatcherAuthorizationService;
import com.smartassist.dispatcher.service.DispatcherServiceResolver;
import com.smartassist.dispatcher.service.JwtService;
import com.smartassist.dispatcher.service.RegistrationService;

@WebMvcTest(LoginController.class)
@Import({
        SecurityConfig.class,
        PasswordConfig.class,
        AuthenticationService.class,
        DispatcherAccessPolicyService.class,
        JwtService.class
})
@TestPropertySource(properties = "dispatcher.jwt-secret=test-jwt-secret-with-sufficient-length-123456")
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthUserRepository authUserRepository;

    @MockitoBean
    private DispatcherProperties dispatcherProperties;

    @MockitoBean
    private DispatcherAuthorizationService dispatcherAuthorizationService;

    @MockitoBean
    private DispatcherServiceResolver dispatcherServiceResolver;

    @MockitoBean
    private RegistrationService registrationService;

    @Test
    void validCredentials_returnsJwtToken() throws Exception {
        AuthUser authUser = AuthUser.builder()
                .id("auth-1")
                .email("admin@smartassist.com")
                .passwordHash(new BCryptPasswordEncoder().encode("secret123"))
                .role("ADMIN")
                .userId("user-1")
                .build();

        when(authUserRepository.findByEmail("admin@smartassist.com"))
                .thenReturn(Optional.of(authUser));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "admin@smartassist.com",
                                  "password": "secret123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.userId").value("user-1"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }
}

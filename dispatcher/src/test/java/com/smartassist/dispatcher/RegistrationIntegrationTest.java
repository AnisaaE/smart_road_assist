package com.smartassist.dispatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.smartassist.dispatcher.model.ApiClient;
import com.smartassist.dispatcher.model.AuthUser;
import com.smartassist.dispatcher.repository.ApiClientRepository;
import com.smartassist.dispatcher.repository.AuthUserRepository;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "dispatcher.user-service-url=http://localhost:18083"
)
@AutoConfigureWebClient
class RegistrationIntegrationTest {

    @LocalServerPort
    private int dispatcherPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private AuthUserRepository authUserRepository;

    @MockitoBean
    private ApiClientRepository apiClientRepository;

    private HttpServer userServiceServer;
    private String forwardedInternalHeader;

    @AfterEach
    void tearDown() {
        if (userServiceServer != null) {
            userServiceServer.stop(0);
        }
    }

    @Test
    void register_createsUserProfileAndAuthIdentity() throws Exception {
        when(authUserRepository.findByEmail("new.user@smartassist.com")).thenReturn(Optional.empty());
        when(authUserRepository.save(any(AuthUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userServiceServer = HttpServer.create(new InetSocketAddress(18083), 0);
        userServiceServer.createContext("/users", this::handleUserRegistration);
        userServiceServer.start();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + dispatcherPort + "/register",
                new org.springframework.http.HttpEntity<>("""
                        {
                          "name": "New User",
                          "email": "new.user@smartassist.com",
                          "phone": "555-0100",
                          "password": "secret123",
                          "role": "USER"
                        }
                        """, headers),
                String.class
        );

        ArgumentCaptor<AuthUser> authUserCaptor = ArgumentCaptor.forClass(AuthUser.class);
        verify(authUserRepository).save(authUserCaptor.capture());

        AuthUser savedAuthUser = authUserCaptor.getValue();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("\"userId\":\"user-99\"");
        assertThat(response.getBody()).contains("\"role\":\"USER\"");
        assertThat(response.getBody()).contains("\"token\":\"");
        assertThat(savedAuthUser.getEmail()).isEqualTo("new.user@smartassist.com");
        assertThat(savedAuthUser.getUserId()).isEqualTo("user-99");
        assertThat(savedAuthUser.getRole()).isEqualTo("USER");
        assertThat(passwordEncoder.matches("secret123", savedAuthUser.getPasswordHash())).isTrue();
        assertThat(forwardedInternalHeader).isEqualTo("smart-road-assist-internal-secret");
    }

    private void handleUserRegistration(HttpExchange exchange) throws IOException {
        forwardedInternalHeader = exchange.getRequestHeaders().getFirst("X-Dispatcher-Secret");
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        writeResponse(exchange, HttpStatus.CREATED.value(), """
                {
                  "id": "user-99",
                  "name": "New User",
                  "email": "new.user@smartassist.com",
                  "phone": "555-0100",
                  "role": "USER",
                  "status": "ACTIVE"
                }
                """);
    }

    private void writeResponse(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
        byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes);
        }
    }
}

package com.smartassist.dispatcher;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.smartassist.dispatcher.service.JwtService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DispatcherListUsersRbacIntegrationTest {

    @LocalServerPort
    private int dispatcherPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtService jwtService;

    private HttpServer userServiceServer;

    @AfterEach
    void tearDown() {
        if (userServiceServer != null) {
            userServiceServer.stop(0);
        }
    }

    @Test
    void adminRole_canListUsers() throws Exception {
        userServiceServer = HttpServer.create(new InetSocketAddress(8083), 0);
        userServiceServer.createContext("/users", this::handleUserListCall);
        userServiceServer.start();

        String token = jwtService.generateToken("admin@smartassist.com", "admin-1", "ADMIN");

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + dispatcherPort + "/api/users",
                HttpMethod.GET,
                authorizedRequest(token),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[{\"id\":\"user-1\"}]");
    }

    @Test
    void userRole_cannotListUsers() {
        String token = jwtService.generateToken("user@smartassist.com", "user-1", "USER");

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + dispatcherPort + "/api/users",
                HttpMethod.GET,
                authorizedRequest(token),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private HttpEntity<Void> authorizedRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

    private void handleUserListCall(HttpExchange exchange) throws IOException {
        writeResponse(exchange, HttpStatus.OK.value(), "[{\"id\":\"user-1\"}]");
    }

    private void writeResponse(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes);
        }
    }
}

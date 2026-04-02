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
class DispatcherRbacIntegrationTest {

    @LocalServerPort
    private int dispatcherPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtService jwtService;

    private HttpServer mechanicServiceServer;
    private HttpServer userServiceServer;

    @AfterEach
    void tearDown() {
        if (mechanicServiceServer != null) {
            mechanicServiceServer.stop(0);
        }
        if (userServiceServer != null) {
            userServiceServer.stop(0);
        }
    }

    @Test
    void userRole_cannotAccessMechanicRoutes() {
        String token = jwtService.generateToken("user@smartassist.com", "user-1", "USER");

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + dispatcherPort + "/api/mechanics",
                HttpMethod.GET,
                authorizedRequest(token),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void mechanicRole_canAccessMechanicRoutes() throws Exception {
        mechanicServiceServer = HttpServer.create(new InetSocketAddress(8081), 0);
        mechanicServiceServer.createContext("/mechanics", this::handleMechanicServiceCall);
        mechanicServiceServer.start();

        String token = jwtService.generateToken("mechanic@smartassist.com", "mechanic-1", "MECHANIC");

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + dispatcherPort + "/api/mechanics",
                HttpMethod.GET,
                authorizedRequest(token),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[{\"id\":\"mech-1\"}]");
    }

    @Test
    void userRole_canReadOnlyOwnProfile() throws Exception {
        userServiceServer = HttpServer.create(new InetSocketAddress(8083), 0);
        userServiceServer.createContext("/users/user-1", this::handleUserProfileCall);
        userServiceServer.start();

        String token = jwtService.generateToken("user@smartassist.com", "user-1", "USER");

        ResponseEntity<String> allowedResponse = restTemplate.exchange(
                "http://localhost:" + dispatcherPort + "/api/users/user-1",
                HttpMethod.GET,
                authorizedRequest(token),
                String.class
        );

        ResponseEntity<String> forbiddenResponse = restTemplate.exchange(
                "http://localhost:" + dispatcherPort + "/api/users/user-2",
                HttpMethod.GET,
                authorizedRequest(token),
                String.class
        );

        assertThat(allowedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(forbiddenResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private HttpEntity<Void> authorizedRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

    private void handleMechanicServiceCall(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        writeResponse(exchange, HttpStatus.OK.value(), "[{\"id\":\"mech-1\"}]");
    }

    private void handleUserProfileCall(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        writeResponse(exchange, HttpStatus.OK.value(),
                "{\"id\":\"user-1\",\"name\":\"Alice\",\"role\":\"USER\",\"status\":\"ACTIVE\"}");
    }

    private void writeResponse(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
        byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes);
        }
    }
}

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartassist.dispatcher.service.JwtService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DispatcherRoutingAndLinksIntegrationTest {

    @LocalServerPort
    private int dispatcherPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private HttpServer paymentServiceServer;
    private HttpServer notificationServiceServer;
    private HttpServer userServiceServer;

    @AfterEach
    void tearDown() {
        stopServer(paymentServiceServer);
        stopServer(notificationServiceServer);
        stopServer(userServiceServer);
    }

    @Test
    void paymentRoute_forwardsAuthenticatedUserHeaders() throws Exception {
        paymentServiceServer = HttpServer.create(new InetSocketAddress(8084), 0);
        paymentServiceServer.createContext("/payments", this::handlePaymentCall);
        paymentServiceServer.start();

        String token = jwtService.generateToken("user@smartassist.com", "user-77", "USER");

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + dispatcherPort + "/api/payments",
                HttpMethod.GET,
                authorizedRequest(token),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("{\"status\":\"ok\"}");
    }

    @Test
    void notificationRoute_forwardsToNotificationService() throws Exception {
        notificationServiceServer = HttpServer.create(new InetSocketAddress(8085), 0);
        notificationServiceServer.createContext("/notifications", this::handleNotificationCall);
        notificationServiceServer.start();

        String token = jwtService.generateToken("user@smartassist.com", "user-77", "USER");

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + dispatcherPort + "/api/notifications",
                HttpMethod.GET,
                authorizedRequest(token),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("{\"delivered\":true}");
    }

    @Test
    void userResponses_includeRoleAwareLinks() throws Exception {
        userServiceServer = HttpServer.create(new InetSocketAddress(8083), 0);
        userServiceServer.createContext("/users/user-1", this::handleUserCall);
        userServiceServer.start();

        String adminToken = jwtService.generateToken("admin@smartassist.com", "admin-1", "ADMIN");
        String userToken = jwtService.generateToken("user@smartassist.com", "user-1", "USER");

        ResponseEntity<String> adminResponse = restTemplate.exchange(
                "http://localhost:" + dispatcherPort + "/api/users/user-1",
                HttpMethod.GET,
                authorizedRequest(adminToken),
                String.class
        );

        ResponseEntity<String> userResponse = restTemplate.exchange(
                "http://localhost:" + dispatcherPort + "/api/users/user-1",
                HttpMethod.GET,
                authorizedRequest(userToken),
                String.class
        );

        JsonNode adminJson = objectMapper.readTree(adminResponse.getBody());
        JsonNode userJson = objectMapper.readTree(userResponse.getBody());

        assertThat(adminResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(adminJson.path("_links").path("self").asText()).isEqualTo("/api/users/user-1");
        assertThat(adminJson.path("_links").path("update").asText()).isEqualTo("/api/users/user-1");
        assertThat(adminJson.path("_links").path("delete").asText()).isEqualTo("/api/users/user-1");
        assertThat(adminJson.path("_links").path("change-role").asText()).isEqualTo("/api/users/user-1/role");

        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userJson.path("_links").path("self").asText()).isEqualTo("/api/users/user-1");
        assertThat(userJson.path("_links").has("update")).isFalse();
        assertThat(userJson.path("_links").has("delete")).isFalse();
    }

    private HttpEntity<Void> authorizedRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

    private void handlePaymentCall(HttpExchange exchange) throws IOException {
        assertThat(exchange.getRequestHeaders().getFirst("X-Dispatcher-Secret"))
                .isEqualTo("smart-road-assist-internal-secret");
        assertThat(exchange.getRequestHeaders().getFirst("X-User-Id")).isEqualTo("user-77");
        assertThat(exchange.getRequestHeaders().getFirst("X-User-Role")).isEqualTo("USER");
        writeResponse(exchange, HttpStatus.OK.value(), "{\"status\":\"ok\"}");
    }

    private void handleNotificationCall(HttpExchange exchange) throws IOException {
        writeResponse(exchange, HttpStatus.OK.value(), "{\"delivered\":true}");
    }

    private void handleUserCall(HttpExchange exchange) throws IOException {
        writeResponse(exchange, HttpStatus.OK.value(),
                "{\"id\":\"user-1\",\"name\":\"Alice\",\"email\":\"alice@smartassist.com\",\"phone\":\"555\",\"role\":\"USER\",\"status\":\"ACTIVE\"}");
    }

    private void writeResponse(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes);
        }
    }

    private void stopServer(HttpServer server) {
        if (server != null) {
            server.stop(0);
        }
    }
}

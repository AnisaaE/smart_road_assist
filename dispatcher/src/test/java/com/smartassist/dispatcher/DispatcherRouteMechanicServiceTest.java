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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "dispatcher.auth-enabled=false"
)
class DispatcherRouteMechanicServiceTest {

    private static final String MECHANIC_SERVICE_RESPONSE = "[{\"id\":\"mech-1\",\"name\":\"Ahmet\"}]";

    @LocalServerPort
    private int dispatcherPort;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpServer mechanicServiceServer;

    @AfterEach
    void tearDown() {
        if (mechanicServiceServer != null) {
            mechanicServiceServer.stop(0);
        }
    }

    @Test
    void getMechanics_routesTrafficToMechanicService() throws Exception {
        mechanicServiceServer = HttpServer.create(new InetSocketAddress(8081), 0);
        mechanicServiceServer.createContext("/mechanics", this::handleMechanicServiceCall);
        mechanicServiceServer.start();

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + dispatcherPort + "/api/mechanics",
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(MECHANIC_SERVICE_RESPONSE);
    }

    private void handleMechanicServiceCall(HttpExchange exchange) throws IOException {
        assertThat(exchange.getRequestURI().getPath()).isEqualTo("/mechanics");
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        writeResponse(exchange, HttpStatus.OK.value(), MECHANIC_SERVICE_RESPONSE);
    }

    private void writeResponse(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
        byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes);
        }
    }
}

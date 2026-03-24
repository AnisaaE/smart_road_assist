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
class DispatcherRouteRequestServiceTest {

    private static final String REQUEST_SERVICE_RESPONSE = "[{\"id\":\"req-1\",\"description\":\"Flat tire\"}]";

    @LocalServerPort
    private int dispatcherPort;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpServer requestServiceServer;

    @AfterEach
    void tearDown() {
        if (requestServiceServer != null) {
            requestServiceServer.stop(0);
        }
    }

    @Test
    void getRequests_routesTrafficToRequestService() throws Exception {
        requestServiceServer = HttpServer.create(new InetSocketAddress(8082), 0);
        requestServiceServer.createContext("/requests", this::handleRequestServiceCall);
        requestServiceServer.start();

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + dispatcherPort + "/api/requests",
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(REQUEST_SERVICE_RESPONSE);
    }

    private void handleRequestServiceCall(HttpExchange exchange) throws IOException {
        assertThat(exchange.getRequestURI().getPath()).isEqualTo("/requests");
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        writeResponse(exchange, HttpStatus.OK.value(), REQUEST_SERVICE_RESPONSE);
    }

    private void writeResponse(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
        byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes);
        }
    }
}

package com.smartassist.dispatcher;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "dispatcher.auth-enabled=false"
)
class DispatcherChunkedResponseTest {

    private static final String REQUEST_SERVICE_RESPONSE = "[{\"id\":\"req-1\",\"description\":\"Chunked response\"}]";

    @LocalServerPort
    private int dispatcherPort;

    private HttpServer requestServiceServer;

    @AfterEach
    void tearDown() {
        if (requestServiceServer != null) {
            requestServiceServer.stop(0);
        }
    }

    @Test
    void getRequests_doesNotDuplicateTransferEncodingHeader() throws Exception {
        requestServiceServer = HttpServer.create(new InetSocketAddress(8082), 0);
        requestServiceServer.createContext("/requests", this::handleChunkedRequestServiceCall);
        requestServiceServer.start();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + dispatcherPort + "/api/requests"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isEqualTo(REQUEST_SERVICE_RESPONSE);
    }

    private void handleChunkedRequestServiceCall(HttpExchange exchange) throws IOException {
        assertThat(exchange.getRequestURI().getPath()).isEqualTo("/requests");
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(HttpStatus.OK.value(), 0);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(REQUEST_SERVICE_RESPONSE.getBytes(StandardCharsets.UTF_8));
        }
    }
}

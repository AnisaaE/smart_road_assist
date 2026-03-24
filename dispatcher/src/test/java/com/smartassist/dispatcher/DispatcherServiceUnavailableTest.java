package com.smartassist.dispatcher;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "dispatcher.auth-enabled=false",
                "dispatcher.request-service-url=http://localhost:18082"
        }
)
class DispatcherServiceUnavailableTest {

    @LocalServerPort
    private int dispatcherPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void requestServiceUnavailable_returnsServiceUnavailable() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + dispatcherPort + "/api/requests",
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }
}

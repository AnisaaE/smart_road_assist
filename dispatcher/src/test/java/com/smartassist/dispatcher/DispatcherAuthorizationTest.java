package com.smartassist.dispatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class DispatcherAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void requestWithoutApiKey_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/requests"))
                .andExpect(status().isUnauthorized());
    }
}

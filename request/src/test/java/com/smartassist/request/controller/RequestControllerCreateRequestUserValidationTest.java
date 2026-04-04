package com.smartassist.request.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.smartassist.request.exception.RequestUserNotFoundException;
import com.smartassist.request.service.RequestService;

@WebMvcTest(RequestController.class)
class RequestControllerCreateRequestUserValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Test
    void createRequestShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(requestService.createRequest(org.mockito.ArgumentMatchers.any()))
                .thenThrow(new RequestUserNotFoundException("User not found: missing-user"));

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": "missing-user",
                                  "type": "BATTERY",
                                  "description": "Battery is dead",
                                  "location": "Downtown garage"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found: missing-user"));
    }
}

package com.smartassist.request.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.smartassist.request.dto.response.RequestResponse;
import com.smartassist.request.model.RequestStatus;
import com.smartassist.request.model.RequestType;
import com.smartassist.request.service.RequestService;

@WebMvcTest(RequestController.class)
class RequestControllerGetRequestByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Test
    void getRequestByIdShouldReturnRequest() throws Exception {
        when(requestService.getRequestById("req-1")).thenReturn(new RequestResponse(
                "req-1",
                "user-123",
                RequestType.BATTERY,
                "Battery is dead",
                "Downtown garage",
                RequestStatus.CREATED,
                null,
                Instant.parse("2026-03-20T10:15:30Z")));

        mockMvc.perform(get("/requests/req-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("req-1"))
                .andExpect(jsonPath("$.type").value("BATTERY"));
    }
}

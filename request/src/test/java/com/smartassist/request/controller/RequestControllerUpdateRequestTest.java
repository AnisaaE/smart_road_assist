package com.smartassist.request.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.smartassist.request.dto.response.RequestResponse;
import com.smartassist.request.model.RequestStatus;
import com.smartassist.request.model.RequestType;
import com.smartassist.request.service.RequestService;

@WebMvcTest(RequestController.class)
class RequestControllerUpdateRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Test
    void updateRequestShouldReturnUpdatedResponse() throws Exception {
        when(requestService.updateRequest(any(), any())).thenReturn(new RequestResponse(
                "req-1",
                "user-123",
                RequestType.TOW,
                "Vehicle needs towing",
                "Highway 5",
                RequestStatus.CREATED,
                null,
                Instant.parse("2026-03-20T10:15:30Z")));

        mockMvc.perform(put("/requests/req-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "type": "TOW",
                                  "description": "Vehicle needs towing",
                                  "location": "Highway 5"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("req-1"))
                .andExpect(jsonPath("$.type").value("TOW"))
                .andExpect(jsonPath("$.description").value("Vehicle needs towing"));
    }
}

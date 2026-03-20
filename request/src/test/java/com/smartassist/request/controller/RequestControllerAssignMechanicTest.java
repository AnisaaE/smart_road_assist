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
class RequestControllerAssignMechanicTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Test
    void assignMechanicShouldReturnAssignedRequest() throws Exception {
        when(requestService.assignMechanic(any(), any())).thenReturn(new RequestResponse(
                "req-1",
                "user-123",
                RequestType.BATTERY,
                "Battery is dead",
                "Downtown garage",
                RequestStatus.ASSIGNED,
                "mech-42",
                Instant.parse("2026-03-20T10:15:30Z")));

        mockMvc.perform(put("/requests/req-1/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "mechanicId": "mech-42"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mechanicId").value("mech-42"))
                .andExpect(jsonPath("$.status").value("ASSIGNED"));
    }

    @Test
    void assignMechanicShouldRejectBlankMechanicId() throws Exception {
        mockMvc.perform(put("/requests/req-1/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "mechanicId": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"));
    }
}

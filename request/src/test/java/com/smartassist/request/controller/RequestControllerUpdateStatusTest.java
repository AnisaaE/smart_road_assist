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
import com.smartassist.request.exception.InvalidRequestStateException;
import com.smartassist.request.model.RequestStatus;
import com.smartassist.request.model.RequestType;
import com.smartassist.request.service.RequestService;

@WebMvcTest(RequestController.class)
class RequestControllerUpdateStatusTest {

    private static final String UPDATE_STATUS_PATH = "/requests/req-1/status";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Test
    void updateStatusShouldReturnRequestWithNewStatus() throws Exception {
        when(requestService.updateStatus(any(), any())).thenReturn(new RequestResponse(
                "req-1",
                "user-123",
                RequestType.BATTERY,
                "Battery is dead",
                "Downtown garage",
                RequestStatus.IN_PROGRESS,
                "mech-42",
                Instant.parse("2026-03-20T10:15:30Z")));

        mockMvc.perform(put(UPDATE_STATUS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "IN_PROGRESS"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.mechanicId").value("mech-42"));
    }

    @Test
    void updateStatusShouldReturnConflictForInvalidTransition() throws Exception {
        when(requestService.updateStatus(any(), any()))
                .thenThrow(new InvalidRequestStateException("Cannot change request status from DONE to IN_PROGRESS"));

        mockMvc.perform(put(UPDATE_STATUS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "IN_PROGRESS"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Cannot change request status from DONE to IN_PROGRESS"));
    }
}

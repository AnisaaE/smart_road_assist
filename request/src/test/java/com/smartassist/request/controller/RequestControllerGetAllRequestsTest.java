package com.smartassist.request.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

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
class RequestControllerGetAllRequestsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Test
    void getAllRequestsShouldReturnRequestList() throws Exception {
        when(requestService.getAllRequests()).thenReturn(List.of(
                new RequestResponse(
                        "req-1",
                        "user-123",
                        RequestType.BATTERY,
                        "Battery is dead",
                        "Downtown garage",
                        RequestStatus.CREATED,
                        null,
                        Instant.parse("2026-03-20T10:15:30Z"))));

        mockMvc.perform(get("/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("req-1"))
                .andExpect(jsonPath("$[0].status").value("CREATED"));
    }
}

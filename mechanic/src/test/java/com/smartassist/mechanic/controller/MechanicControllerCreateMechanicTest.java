package com.smartassist.mechanic.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.smartassist.mechanic.dto.response.MechanicResponse;
import com.smartassist.mechanic.service.MechanicService;

@WebMvcTest(MechanicController.class)
class MechanicControllerCreateMechanicTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MechanicService mechanicService;

    @Test
    void createMechanicShouldReturnCreatedResponse() throws Exception {
        when(mechanicService.createMechanic(any())).thenReturn(new MechanicResponse(
                "mech-1",
                "Jane Doe",
                "+90-555-0101",
                List.of("BATTERY", "TOWING"),
                "Istanbul",
                Instant.parse("2026-03-20T10:15:30Z")));

        mockMvc.perform(post("/mechanics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Jane Doe",
                                  "phoneNumber": "+90-555-0101",
                                  "specialties": ["BATTERY", "TOWING"],
                                  "serviceArea": "Istanbul"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("mech-1"))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.specialties[0]").value("BATTERY"));
    }

    @Test
    void createMechanicShouldRejectInvalidPayload() throws Exception {
        mockMvc.perform(post("/mechanics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "",
                                  "phoneNumber": "",
                                  "specialties": [],
                                  "serviceArea": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"));
    }
}

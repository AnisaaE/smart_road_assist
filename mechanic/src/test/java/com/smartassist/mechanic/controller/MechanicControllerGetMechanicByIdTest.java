package com.smartassist.mechanic.controller;

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

import com.smartassist.mechanic.dto.response.MechanicResponse;
import com.smartassist.mechanic.service.MechanicService;

@WebMvcTest(MechanicController.class)
class MechanicControllerGetMechanicByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MechanicService mechanicService;

    @Test
    void getMechanicByIdShouldReturnMechanicResponse() throws Exception {
        when(mechanicService.getMechanicById("mech-1")).thenReturn(new MechanicResponse(
                "mech-1",
                "Jane Doe",
                "+90-555-0101",
                List.of("BATTERY"),
                "Istanbul",
                Instant.parse("2026-03-20T10:15:30Z")));

        mockMvc.perform(get("/mechanics/mech-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("mech-1"))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.serviceArea").value("Istanbul"));
    }
}

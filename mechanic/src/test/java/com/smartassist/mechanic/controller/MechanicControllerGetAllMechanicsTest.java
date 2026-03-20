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
class MechanicControllerGetAllMechanicsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MechanicService mechanicService;

    @Test
    void getAllMechanicsShouldReturnMechanicResponses() throws Exception {
        when(mechanicService.getAllMechanics()).thenReturn(List.of(
                new MechanicResponse(
                        "mech-1",
                        "Jane Doe",
                        "+90-555-0101",
                        List.of("BATTERY"),
                        "Istanbul",
                        Instant.parse("2026-03-20T10:15:30Z")),
                new MechanicResponse(
                        "mech-2",
                        "John Doe",
                        "+90-555-0102",
                        List.of("TOWING"),
                        "Ankara",
                        Instant.parse("2026-03-20T10:16:30Z"))));

        mockMvc.perform(get("/mechanics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("mech-1"))
                .andExpect(jsonPath("$[1].name").value("John Doe"));
    }
}

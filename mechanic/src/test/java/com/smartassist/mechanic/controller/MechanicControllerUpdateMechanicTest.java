package com.smartassist.mechanic.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
class MechanicControllerUpdateMechanicTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MechanicService mechanicService;

    @Test
    void updateMechanicShouldReturnUpdatedResponse() throws Exception {
        when(mechanicService.updateMechanic(eq("mech-1"), any())).thenReturn(new MechanicResponse(
                "mech-1",
                "Jane Smith",
                "+90-555-0202",
                List.of("TOWING", "LOCKOUT"),
                "Ankara",
                Instant.parse("2026-03-20T10:15:30Z")));

        mockMvc.perform(put("/mechanics/mech-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Jane Smith",
                                  "phoneNumber": "+90-555-0202",
                                  "specialties": ["TOWING", "LOCKOUT"],
                                  "serviceArea": "Ankara"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("mech-1"))
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.serviceArea").value("Ankara"));
    }
}

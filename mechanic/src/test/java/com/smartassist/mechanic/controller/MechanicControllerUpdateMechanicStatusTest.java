package com.smartassist.mechanic.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.smartassist.mechanic.dto.response.MechanicStateResponse;
import com.smartassist.mechanic.model.MechanicStatus;
import com.smartassist.mechanic.service.MechanicService;

@WebMvcTest(MechanicController.class)
class MechanicControllerUpdateMechanicStatusTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MechanicService mechanicService;

    @Test
    void updateMechanicStatusShouldReturnUpdatedState() throws Exception {
        when(mechanicService.updateMechanicStatus(eq("mech-1"), any())).thenReturn(
                new MechanicStateResponse("mech-1", MechanicStatus.AVAILABLE, null, null));

        mockMvc.perform(put("/mechanics/mech-1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "AVAILABLE"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mechanicId").value("mech-1"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }
}

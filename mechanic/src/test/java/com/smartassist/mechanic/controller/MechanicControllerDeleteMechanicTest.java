package com.smartassist.mechanic.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.smartassist.mechanic.service.MechanicService;

@WebMvcTest(MechanicController.class)
class MechanicControllerDeleteMechanicTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MechanicService mechanicService;

    @Test
    void deleteMechanicShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/mechanics/mech-1"))
                .andExpect(status().isNoContent());
    }
}

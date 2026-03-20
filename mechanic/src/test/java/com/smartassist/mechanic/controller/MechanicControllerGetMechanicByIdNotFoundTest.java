package com.smartassist.mechanic.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.smartassist.mechanic.exception.MechanicNotFoundException;
import com.smartassist.mechanic.service.MechanicService;

@WebMvcTest(MechanicController.class)
class MechanicControllerGetMechanicByIdNotFoundTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MechanicService mechanicService;

    @Test
    void getMechanicByIdShouldReturnNotFoundWhenMechanicDoesNotExist() throws Exception {
        when(mechanicService.getMechanicById("missing-id"))
                .thenThrow(new MechanicNotFoundException("Mechanic not found: missing-id"));

        mockMvc.perform(get("/mechanics/missing-id"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Mechanic not found: missing-id"));
    }
}

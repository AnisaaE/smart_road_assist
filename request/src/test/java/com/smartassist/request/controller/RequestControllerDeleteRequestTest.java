package com.smartassist.request.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.smartassist.request.service.RequestService;

@WebMvcTest(RequestController.class)
class RequestControllerDeleteRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Test
    void deleteRequestShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/requests/req-1"))
                .andExpect(status().isNoContent());

        verify(requestService).deleteRequest("req-1");
    }
}

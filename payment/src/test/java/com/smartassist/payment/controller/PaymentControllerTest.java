package com.smartassist.payment.controller;

import com.smartassist.payment.service.PaymentService; // Servis importu
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType; // <-- YENİ EKLEDİĞİN
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void shouldReturn201WhenPaymentCreated() throws Exception {
        String paymentJson = "{\"amount\": 100.0, \"status\": \"SUCCESS\"}"; 

        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paymentJson))
                .andExpect(status().isCreated());
    }
}
package com.smartassist.payment.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class) // Şimdi bu sınıfı tanıyacak çünkü aynı paketteler
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn201WhenPaymentCreated() throws Exception {
        mockMvc.perform(post("/payments"))
               .andExpect(status().isCreated()); // 201 bekliyoruz ama 405/404 gelecek
    }
}
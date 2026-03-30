package com.smartassist.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartassist.payment.dto.PaymentRequestDTO;
import com.smartassist.payment.dto.PaymentResponseDTO;
import com.smartassist.payment.service.IPaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
public class PaymentHateoasTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IPaymentService paymentService;

    @Test
    @DisplayName("POST /payments yanıtı HATEOAS linklerini (_links.self) içermeli")
    void shouldHaveSelfLinkOnCreate() throws Exception {
        // GIVEN
        PaymentRequestDTO request = new PaymentRequestDTO(
                "req-001", "user-001", new BigDecimal("100.00"), "CASH"
        );
        
        // Response DTO (Henüz link ekleme mantığı kodda yok!)
        PaymentResponseDTO response = new PaymentResponseDTO(
                "pay-001", "req-001", "user-001", new BigDecimal("100.00"), "CASH", "PENDING"
        );
        
        Mockito.when(paymentService.createPayment(Mockito.any())).thenReturn(response);

        // WHEN & THEN
        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                // BU SATIR HATA VERECEK (RED):
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links['update-status'].href").exists());
    }
}
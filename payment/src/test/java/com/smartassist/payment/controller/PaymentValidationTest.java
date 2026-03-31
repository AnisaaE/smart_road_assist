package com.smartassist.payment.controller;

import com.smartassist.payment.dto.PaymentRequestDTO;
import com.smartassist.payment.service.IPaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IPaymentService paymentService;

    @Test
    @DisplayName("Negatif tutar ile ödeme isteği 400 Bad Request dönmeli")
    void shouldReturn400WhenAmountIsNegative() throws Exception {
        // GIVEN: Geçersiz (negatif) tutar
        PaymentRequestDTO invalidRequest = PaymentRequestDTO.builder()
                .requestId("req-123")
                .userId("user-123")
                .amount(new BigDecimal("-50.00")) 
                .paymentMethod("CREDIT_CARD")
                .build();

        // WHEN & THEN
        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest()); // 400 bekliyoruz
    }

    @Test
    @DisplayName("Boş User ID ile ödeme isteği 400 Bad Request dönmeli")
    void shouldReturn400WhenUserIdIsBlank() throws Exception {
        // GIVEN: userId boş
        PaymentRequestDTO invalidRequest = PaymentRequestDTO.builder()
                .requestId("req-123")
                .userId("") 
                .amount(new BigDecimal("100.00"))
                .paymentMethod("CASH")
                .build();

        // WHEN & THEN
        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
package com.smartassist.payment.controller;

import com.smartassist.payment.dto.PaymentResponseDTO;
import com.smartassist.payment.exception.PaymentNotFoundException;
import com.smartassist.payment.service.IPaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TDD RED → GREEN
 * GET /payments/{id}
 * GET /payments/request/{requestId}  ← dispatcher ödeme durumunu sorgular
 */
@WebMvcTest(PaymentController.class)
public class PaymentGetTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPaymentService paymentService;

    // ─── GET by payment id ────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /payments/{id} - mevcut id ile 200 ve DTO dönmeli")
    void shouldReturnPaymentById() throws Exception {
        // GIVEN
        PaymentResponseDTO response = new PaymentResponseDTO(
                "pay-001", "req-001", "user-001",
                new BigDecimal("350.00"), "CREDIT_CARD", "PAID"
        );
        Mockito.when(paymentService.getPaymentById("pay-001")).thenReturn(response);

        // WHEN & THEN
        mockMvc.perform(get("/payments/pay-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("pay-001"))
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("GET /payments/{id} - olmayan id ile 404 dönmeli")
    void shouldReturn404WhenPaymentNotFound() throws Exception {
        // GIVEN
        Mockito.when(paymentService.getPaymentById("not-exist"))
                .thenThrow(new PaymentNotFoundException("Payment not found: not-exist"));

        // WHEN & THEN
        mockMvc.perform(get("/payments/not-exist"))
                .andExpect(status().isNotFound());
    }

    // ─── GET by request id ────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /payments/request/{requestId} - request bazlı ödeme 200 dönmeli")
    void shouldReturnPaymentByRequestId() throws Exception {
        // GIVEN: Dispatcher iş tamamlandıktan sonra ödemeyi bu endpoint'ten sorgular
        PaymentResponseDTO response = new PaymentResponseDTO(
                "pay-001", "req-001", "user-001",
                new BigDecimal("350.00"), "CREDIT_CARD", "PENDING"
        );
        Mockito.when(paymentService.getPaymentByRequestId("req-001")).thenReturn(response);

        // WHEN & THEN
        mockMvc.perform(get("/payments/request/req-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value("req-001"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("GET /payments/request/{requestId} - olmayan requestId ile 404 dönmeli")
    void shouldReturn404WhenRequestIdNotFound() throws Exception {
        // GIVEN
        Mockito.when(paymentService.getPaymentByRequestId("req-999"))
                .thenThrow(new PaymentNotFoundException("No payment for request: req-999"));

        // WHEN & THEN
        mockMvc.perform(get("/payments/request/req-999"))
                .andExpect(status().isNotFound());
    }
}
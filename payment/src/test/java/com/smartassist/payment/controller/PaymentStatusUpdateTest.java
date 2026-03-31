package com.smartassist.payment.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
public class PaymentStatusUpdateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPaymentService paymentService;

    @Test
    @DisplayName("PATCH /payments/{id}/status başarılı bir güncelleme ve HATEOAS linkleri dönmeli")
    void shouldUpdateStatusAndReturnLinks() throws Exception {
        // GIVEN
        String paymentId = "pay-123";
        String newStatus = "PAID";
        
        PaymentResponseDTO mockResponse = PaymentResponseDTO.builder()
                .id(paymentId)
                .status(newStatus)
                .requestId("req-001")
                .build();

        Mockito.when(paymentService.updatePaymentStatus(Mockito.eq(paymentId), Mockito.eq(newStatus)))
                .thenReturn(mockResponse);

        // WHEN & THEN
        mockMvc.perform(patch("/payments/{id}/status", paymentId)
                        .param("value", newStatus) // @RequestParam String value
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"))
                // HATEOAS Kontrolleri
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links['update-status'].href").exists());
    }
}
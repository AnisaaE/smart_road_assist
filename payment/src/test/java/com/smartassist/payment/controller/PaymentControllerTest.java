package com.smartassist.payment.controller;

import com.smartassist.payment.dto.PaymentResponseDTO; // EKLENDİ
import com.smartassist.payment.service.IPaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito; // EKLENDİ
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPaymentService paymentService; 

    @Test
    void shouldReturn201WhenPaymentCreated() throws Exception {
        // GIVEN: Servis boş dönmesin diye bir mock response hazırlıyoruz
        PaymentResponseDTO mockResponse = PaymentResponseDTO.builder()
                .id("pay-123")
                .status("PENDING")
                .build();
        
        Mockito.when(paymentService.createPayment(Mockito.any())).thenReturn(mockResponse);

        // PaymentRequestDTO formatına uygun JSON
        String paymentJson = """
                {
                    "requestId": "req-123",
                    "userId": "user-456",
                    "amount": 100.0,
                    "paymentMethod": "CREDIT_CARD"
                }
                """; 

        // WHEN & THEN
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paymentJson))
                .andExpect(status().isCreated());
    }
}
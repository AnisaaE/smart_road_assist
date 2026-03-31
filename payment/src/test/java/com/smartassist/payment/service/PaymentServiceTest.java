package com.smartassist.payment.service;

import com.smartassist.payment.dto.PaymentRequestDTO;
import com.smartassist.payment.dto.PaymentResponseDTO;
import com.smartassist.payment.model.Payment;
import com.smartassist.payment.repository.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("Yeni ödeme oluşturulduğunda statüsü PENDING olmalı")
    void shouldCreatePaymentWithPendingStatus() {
        // 1. GIVEN: DTO'yu oluştururken parametre sırasına dikkat!
        // (requestId, userId, amount, paymentMethod)
        PaymentRequestDTO request = new PaymentRequestDTO(
                "req-1", 
                "user-1", 
                new BigDecimal("100.00"), 
                "CASH"
        );

        // 2. Mock nesnesini Builder ile oluştur (Eğer Payment sınıfında @Builder varsa)
        Payment savedPayment = Payment.builder()
                .id("pay-123")
                .requestId("req-1")
                .userId("user-1")
                .amount(new BigDecimal("100.00"))
                .paymentMethod("CASH")
                .status("PENDING")
                .build();

        Mockito.when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        // 3. WHEN
        PaymentResponseDTO response = paymentService.createPayment(request);

        // 4. THEN
        assertThat(response.getStatus()).isEqualTo("PENDING");
        assertThat(response.getId()).isEqualTo("pay-123");
        Mockito.verify(paymentRepository, Mockito.times(1)).save(any(Payment.class));
    }
}
package com.smartassist.payment.service;

import com.smartassist.payment.dto.PaymentRequestDTO;
import com.smartassist.payment.exception.PaymentAlreadyExistsException; // Bu sınıfı birazdan oluşturacağız
import com.smartassist.payment.repository.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentIdempotencyTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("Aynı requestId ile ikinci kez ödeme oluşturulmaya çalışıldığında hata fırlatmalı")
    void whenDuplicateRequestId_thenThrowPaymentAlreadyExistsException() {
        // GIVEN
        String duplicateId = "req-12345";
        PaymentRequestDTO request = PaymentRequestDTO.builder()
                .requestId(duplicateId)
                .userId("user-1")
                .amount(new BigDecimal("100.00"))
                .paymentMethod("CARD")
                .build();

        // Repository'de bu requestId'nin zaten var olduğunu simüle ediyoruz
        when(paymentRepository.existsByRequestId(duplicateId)).thenReturn(true);

        // WHEN & THEN: Servis metodu çağrıldığında exception bekliyoruz
        assertThrows(PaymentAlreadyExistsException.class, () -> {
            paymentService.createPayment(request);
        });

        // Doğrulama: Hata fırladığı için veritabanına kaydetme (save) işlemi ASLA yapılmamalı
        verify(paymentRepository, never()).save(any());
    }
}
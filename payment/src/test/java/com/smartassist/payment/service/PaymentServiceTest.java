package com.smartassist.payment.service;

import com.smartassist.payment.dto.PaymentRequestDTO;
import com.smartassist.payment.dto.PaymentResponseDTO;
import com.smartassist.payment.model.Payment;
import com.smartassist.payment.model.PaymentStatus;
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
                .status(PaymentStatus.PENDING)
                .build();

        Mockito.when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        // 3. WHEN
        PaymentResponseDTO response = paymentService.createPayment(request);

        // 4. THEN
        assertThat(response.getStatus()).isEqualTo("PENDING");
        assertThat(response.getId()).isEqualTo("pay-123");
        Mockito.verify(paymentRepository, Mockito.times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Ödeme durumu başarıyla güncellenebilmeli")
    void shouldUpdateStatusSuccessfully() {
        // GIVEN
        Payment existingPayment = Payment.builder()
                .id("pay-123")
                .status(PaymentStatus.PENDING)
                .build();

        Mockito.when(paymentRepository.findById("pay-123")).thenReturn(java.util.Optional.of(existingPayment));
        Mockito.when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        PaymentResponseDTO response = paymentService.updatePaymentStatus("pay-123", "PAID");

        // THEN
        assertThat(response.getStatus()).isEqualTo(PaymentStatus.PAID.name());
        Mockito.verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("Terminal state (PAID) olan bir ödeme güncellenemez, hata fırlatır")
    void shouldThrowExceptionWhenUpdatingFromTerminalState() {
        // GIVEN
        Payment paidPayment = Payment.builder()
                .id("pay-123")
                .status(PaymentStatus.PAID) // Zaten ödenmiş!
                .build();

        Mockito.when(paymentRepository.findById("pay-123")).thenReturn(java.util.Optional.of(paidPayment));

        // WHEN & THEN
        org.junit.jupiter.api.Assertions.assertThrows(
            com.smartassist.payment.exception.InvalidPaymentStatusException.class, 
            () -> paymentService.updatePaymentStatus("pay-123", "FAILED")
        );
    }

        @Test
    @DisplayName("Olmayan bir ID ile ödeme sorgulandığında PaymentNotFoundException fırlatmalı")
    void shouldThrowExceptionWhenPaymentIdNotFound() {
        // GIVEN: Repository boş dönecek
        Mockito.when(paymentRepository.findById("not-exists")).thenReturn(java.util.Optional.empty());

        // WHEN & THEN: Metot çağrıldığında bu hatayı bekliyoruz
        org.junit.jupiter.api.Assertions.assertThrows(PaymentNotFoundException.class, () -> {
            paymentService.getPaymentById("not-exists");
        });
    }
}
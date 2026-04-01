package com.smartassist.payment.repository;

import com.smartassist.payment.model.Payment;
import com.smartassist.payment.model.PaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest // Sadece MongoDB katmanını test eder
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("RequestId ile ödeme kaydı başarıyla bulunabilmeli")
    void shouldFindPaymentByRequestId() {
        // GIVEN: Veritabanına bir kayıt atalım
        Payment payment = Payment.builder()
                .id("pay-999")
                .requestId("req-unique-001")
                .userId("user-1")
                .amount(new BigDecimal("250.00"))
                .status(PaymentStatus.PENDING)
                .build();
        paymentRepository.save(payment);

        // WHEN: RequestId ile sorgulayalım
        Optional<Payment> found = paymentRepository.findByRequestId("req-unique-001");

        // THEN: Kayıt gelmeli ve veriler eşleşmeli
        assertThat(found).isPresent();
        assertThat(found.get().getRequestId()).isEqualTo("req-unique-001");
        assertThat(found.get().getAmount()).isEqualByComparingTo("250.00");
    }

    @Test
    @DisplayName("Olmayan bir RequestId sorgulandığında boş Optional dönmeli")
    void shouldReturnEmptyWhenRequestIdNotFound() {
        // WHEN
        Optional<Payment> found = paymentRepository.findByRequestId("not-exists-id");

        // THEN
        assertThat(found).isEmpty();
    }
}
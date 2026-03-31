ppackage com.smartassist.payment.service;

import com.smartassist.payment.dto.PaymentRequestDTO;
import com.smartassist.payment.dto.PaymentResponseDTO;
import com.smartassist.payment.model.Payment;
import com.smartassist.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private PaymentService paymentService; // IPaymentService'i implement eden sınıf

    @Test
    @DisplayName("Yeni ödeme oluşturulduğunda statüsü PENDING olmalı")
    void shouldCreatePaymentWithPendingStatus() {
        // GIVEN
        PaymentRequestDTO request = new PaymentRequestDTO(
                "req-1", "user-1", new BigDecimal("100.00"), "CASH"
        );

        // Repository save edildiğinde dönecek olan mock nesne
        Payment savedPayment = new Payment();
        savedPayment.setId("pay-123");
        savedPayment.setStatus("PENDING");
        savedPayment.setAmount(new BigDecimal("100.00"));

        Mockito.when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        // WHEN
        PaymentResponseDTO response = paymentService.createPayment(request);

        // THEN
        assertThat(response.getStatus()).isEqualTo("PENDING");
        assertThat(response.getId()).isNotNull();
        Mockito.verify(paymentRepository, Mockito.times(1)).save(any(Payment.class));
    }
}
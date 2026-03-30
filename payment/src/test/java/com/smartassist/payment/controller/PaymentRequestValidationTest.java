package com.smartassist.payment.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Geçerli bir DTO için validasyon hatası olmamalı")
    void shouldPassValidationWithValidRequest() {
        // GIVEN: Attığın kodlardaki başarılı senaryo verisi
        PaymentRequestDTO request = new PaymentRequestDTO(
                "req-001", "user-001", new BigDecimal("350.00"), "CREDIT_CARD"
        );

        // WHEN
        Set<ConstraintViolation<PaymentRequestDTO>> violations = validator.validate(request);

        // THEN
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("requestId boş olduğunda hata vermeli")
    void shouldFailWhenRequestIdIsBlank() {
        // GIVEN: shouldReturn400WhenRequestIdIsBlank senaryosu
        PaymentRequestDTO request = new PaymentRequestDTO(
                "", "user-001", new BigDecimal("100.00"), "CASH"
        );

        // WHEN
        Set<ConstraintViolation<PaymentRequestDTO>> violations = validator.validate(request);

        // THEN
        assertThat(violations).isNotEmpty();
        // requestId alanından dolayı hata geldiğini doğrula
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("requestId"));
    }

    @Test
    @DisplayName("amount negatif olduğunda hata vermeli")
    void shouldFailWhenAmountIsNegative() {
        // GIVEN: shouldReturn400WhenAmountIsNegative senaryosu
        PaymentRequestDTO request = new PaymentRequestDTO(
                "req-001", "user-001", new BigDecimal("-50.00"), "CASH"
        );

        // WHEN
        Set<ConstraintViolation<PaymentRequestDTO>> violations = validator.validate(request);

        // THEN
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("amount"));
    }
}
package com.smartassist.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

/**
 * POST /payments isteğinde gelen veri.
 * Dispatcher, iş tamamlandıktan sonra bu DTO'yu gönderir.
 */
public class PaymentRequestDTO {

    @NotBlank(message = "requestId cannot be blank")
    private String requestId;

    @NotBlank(message = "userId cannot be blank")
    private String userId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "paymentMethod cannot be blank")
    @Pattern(
        regexp = "^(CREDIT_CARD|DEBIT_CARD|CASH)$",
        message = "paymentMethod must be one of: CREDIT_CARD, DEBIT_CARD, CASH"
    )
    private String paymentMethod;

    // ─── Constructors ─────────────────────────────────────────────────────────

    public PaymentRequestDTO() {}

    public PaymentRequestDTO(String requestId, String userId,
                              BigDecimal amount, String paymentMethod) {
        this.requestId     = requestId;
        this.userId        = userId;
        this.amount        = amount;
        this.paymentMethod = paymentMethod;
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public String getRequestId()      { return requestId; }
    public void setRequestId(String v){ this.requestId = v; }

    public String getUserId()         { return userId; }
    public void setUserId(String v)   { this.userId = v; }

    public BigDecimal getAmount()     { return amount; }
    public void setAmount(BigDecimal v){ this.amount = v; }

    public String getPaymentMethod()       { return paymentMethod; }
    public void setPaymentMethod(String v) { this.paymentMethod = v; }
}
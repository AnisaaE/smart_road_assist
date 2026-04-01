package com.smartassist.payment.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class PaymentRequestDTO {

    @NotBlank(message = "Request ID cannot be blank")
    private String requestId;

    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

}
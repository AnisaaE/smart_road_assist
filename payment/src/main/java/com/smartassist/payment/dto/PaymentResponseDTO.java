package com.smartassist.payment.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO extends RepresentationModel<PaymentResponseDTO> {
    private String id;
    private String requestId;
    private String userId;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
}
package com.smartassist.payment.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Document(collection = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    private String id;
    private String requestId;
    private String userId;
    private BigDecimal amount;
    private String paymentMethod;
    private String status; // PENDING, PAID, FAILED
}
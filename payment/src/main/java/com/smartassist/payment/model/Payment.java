package com.smartassist.payment.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Payment {
    @Id
    private String id;
    private double amount;
    private String status;
}
package com.smartassist.payment.service;

import com.smartassist.payment.model.Payment;

public interface PaymentService {
    Payment createPayment(Payment payment);
}
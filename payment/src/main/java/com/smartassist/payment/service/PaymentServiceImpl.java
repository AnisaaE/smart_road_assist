package com.smartassist.payment.service;

import com.smartassist.payment.model.Payment;
import com.smartassist.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment createPayment(Payment payment) {
        payment.setStatus("COMPLETED"); // Basit bir iş mantığı
        return paymentRepository.save(payment);
    }
}
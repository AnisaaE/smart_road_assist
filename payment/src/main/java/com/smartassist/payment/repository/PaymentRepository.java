package com.smartassist.payment.repository;

import com.smartassist.payment.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    // Testin (shouldReturnPaymentByRequestId) çalışması için bu şart:
    Optional<Payment> findByRequestId(String requestId);
}
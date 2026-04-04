package com.smartassist.payment.repository;

import com.smartassist.payment.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    
    // BU SATIRI EKLE: Spring senin için bu sorguyu otomatik oluşturacak
    boolean existsByRequestId(String requestId);

    Optional<Payment> findByRequestId(String requestId);
}
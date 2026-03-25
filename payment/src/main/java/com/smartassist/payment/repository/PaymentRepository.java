package com.smartassist.payment.repository;

import com.smartassist.payment.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    // MongoDB CRUD işlemleri otomatik geldi!
}
package com.smartassist.payment.service;

import com.smartassist.payment.dto.PaymentRequestDTO;
import com.smartassist.payment.dto.PaymentResponseDTO;

/**
 * TDD GREEN: Controller'ın ihtiyaç duyduğu tüm metodlar burada tanımlı olmalı.
 */
public interface IPaymentService {

    // POST /payments için
    PaymentResponseDTO createPayment(PaymentRequestDTO dto);

    // GET /payments/{id} için
    PaymentResponseDTO getPaymentById(String id);

    // PATCH /payments/{id}/status için
    PaymentResponseDTO updatePaymentStatus(String id, String newStatus);

    // İhtiyaç duyarsan (Testlerinde vardı):
    PaymentResponseDTO getPaymentByRequestId(String requestId);
}
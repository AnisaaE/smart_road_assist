package com.smartassist.payment.service;

import com.smartassist.payment.dto.PaymentRequestDTO;
import com.smartassist.payment.dto.PaymentResponseDTO;

public interface IPaymentService {
    PaymentResponseDTO createPayment(PaymentRequestDTO dto);
    PaymentResponseDTO getPaymentById(String id);
    PaymentResponseDTO updatePaymentStatus(String id, String newStatus);
    PaymentResponseDTO getPaymentByRequestId(String requestId);
}
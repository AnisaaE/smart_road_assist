package com.smartassist.payment.service;

import com.smartassist.payment.dto.PaymentRequestDTO;
import com.smartassist.payment.dto.PaymentResponseDTO;
import com.smartassist.payment.model.Payment;
import com.smartassist.payment.model.PaymentStatus;
import com.smartassist.payment.repository.PaymentRepository;
import com.smartassist.payment.exception.PaymentNotFoundException;
import com.smartassist.payment.exception.InvalidPaymentStatusException;
import com.smartassist.payment.exception.PaymentAlreadyExistsException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final PaymentRepository repository;

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
        // Kontrol: requestId daha önce kullanılmış mı? (Idempotency)
        if (repository.existsByRequestId(dto.getRequestId())) {
            throw new PaymentAlreadyExistsException("Payment already exists with request id: " + dto.getRequestId());
        }

        Payment payment = Payment.builder()
                .id(UUID.randomUUID().toString())
                .requestId(dto.getRequestId())
                .userId(dto.getUserId())
                .amount(dto.getAmount())
                .paymentMethod(dto.getPaymentMethod())
                .status(PaymentStatus.PENDING)
                .build();

        return mapToResponse(repository.save(payment));
    }

    @Override 
    public PaymentResponseDTO getPaymentById(String id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + id));
    }

    @Override 
    public PaymentResponseDTO updatePaymentStatus(String id, String newStatus) {
        Payment payment = repository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + id));

        // Terminal state kontrolü
        if (payment.getStatus() == PaymentStatus.PAID || payment.getStatus() == PaymentStatus.FAILED) {
            throw new InvalidPaymentStatusException("Cannot change status of a terminal state: " + payment.getStatus());
        }

        try {
            payment.setStatus(PaymentStatus.valueOf(newStatus.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new InvalidPaymentStatusException("Invalid status value: " + newStatus);
        }
        
        return mapToResponse(repository.save(payment));
    }

    @Override
    public PaymentResponseDTO getPaymentByRequestId(String requestId) {
        return repository.findByRequestId(requestId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new PaymentNotFoundException("No payment found for Request ID: " + requestId));
    }

    private PaymentResponseDTO mapToResponse(Payment p) {
        return PaymentResponseDTO.builder()
                .id(p.getId())
                .requestId(p.getRequestId())
                .userId(p.getUserId())
                .amount(p.getAmount())
                .paymentMethod(p.getPaymentMethod())
                .status(p.getStatus().name())
                .build();
    }
}
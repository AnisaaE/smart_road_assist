package com.smartassist.payment.service;

import com.smartassist.payment.dto.PaymentRequestDTO;
import com.smartassist.payment.dto.PaymentResponseDTO;
import com.smartassist.payment.model.Payment;
import com.smartassist.payment.model.PaymentStatus;
import com.smartassist.payment.repository.PaymentRepository;
import com.smartassist.payment.exception.PaymentNotFoundException;
import com.smartassist.payment.exception.InvalidPaymentStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final PaymentRepository repository;

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
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

        if (payment.getStatus() == PaymentStatus.PAID || payment.getStatus() == PaymentStatus.FAILED) {
            throw new InvalidPaymentStatusException("Cannot change status of a terminal state: " + payment.getStatus());
        }

        payment.setStatus(PaymentStatus.valueOf(newStatus.toUpperCase()));
        return mapToResponse(repository.save(payment));
    }

    @Override 
    public PaymentResponseDTO getPaymentByRequestId(String requestId) {
        return repository.findByRequestId(requestId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for request: " + requestId));
    }

    private PaymentResponseDTO mapToResponse(Payment p) {
        return PaymentResponseDTO.builder()
                .id(p.getId())
                .requestId(p.getRequestId())
                .userId(p.getUserId())
                .amount(p.getAmount())
                .paymentMethod(p.getPaymentMethod())
                .status(p.getStatus().name()) // Enum -> String dönüşümü unutma!
                .build();
    }
}
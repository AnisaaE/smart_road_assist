package com.smartassist.payment.service;

import com.smartassist.payment.dto.PaymentRequestDTO;
import com.smartassist.payment.dto.PaymentResponseDTO;
import com.smartassist.payment.model.Payment;
import com.smartassist.payment.model.PaymentStatus;
import com.smartassist.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final PaymentRepository repository;

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
        // 1. Yeni bir Entity oluştur ve PENDING setle (GREEN için en kritik adım)
        Payment payment = Payment.builder()
                .id(UUID.randomUUID().toString())
                .requestId(dto.getRequestId())     // .requestId() değil, get ile çağır
                .userId(dto.getUserId())           // .userId() değil, get ile çağır
                .amount(dto.getAmount())           // .amount() değil, get ile çağır
                .paymentMethod(dto.getPaymentMethod()) // .paymentMethod() değil, get ile çağır
                .status(PaymentStatus.PENDING)
                .build();

        // 2. Veritabanına kaydet
        Payment savedPayment = repository.save(payment);

        // 3. Response DTO'ya map et
        return mapToResponse(savedPayment);
    }

    // IPaymentService'den gelen diğer metodları şimdilik boş bırakabilirsin
    @Override public PaymentResponseDTO getPaymentById(String id) { return null; }
    @Override public PaymentResponseDTO updatePaymentStatus(String id, String s) { return null; }
    @Override public PaymentResponseDTO getPaymentByRequestId(String r) { return null; }

    private PaymentResponseDTO mapToResponse(Payment p) {
        return PaymentResponseDTO.builder()
                .id(p.getId())
                .requestId(p.getRequestId())
                .userId(p.getUserId())
                .amount(p.getAmount())
                .paymentMethod(p.getPaymentMethod())
                .status(p.getStatus())
                .build();
    }
}
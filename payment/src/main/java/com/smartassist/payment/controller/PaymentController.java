package com.smartassist.payment.controller;

import com.smartassist.payment.dto.PaymentRequestDTO;
import com.smartassist.payment.dto.PaymentResponseDTO;
import com.smartassist.payment.service.IPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(addHateoasLinks(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable String id) {
        PaymentResponseDTO response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(addHateoasLinks(response));
    }
    
    @GetMapping("/request/{requestId}")
public ResponseEntity<PaymentResponseDTO> getPaymentByRequestId(@PathVariable String requestId) {
    // 1. Servisten veriyi al
    PaymentResponseDTO response = paymentService.getPaymentByRequestId(requestId);
    
    // 2. HATEOAS Linklerini ekle (addHateoasLinks metodun hazır olmalı)
    return ResponseEntity.ok(addHateoasLinks(response));
}
    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(
            @PathVariable String id, 
            @RequestParam String value) {
        
        PaymentResponseDTO response = paymentService.updatePaymentStatus(id, value);
        return ResponseEntity.ok(addHateoasLinks(response));
    }

    /**
     * RMM Level 3: HATEOAS Linklerini ekleyen yardımcı metod.
     * Kod tekrarını önlemek için merkezi bir yere aldık.
     */
    private PaymentResponseDTO addHateoasLinks(PaymentResponseDTO dto) {
        // 1. Kendi linki (Self)
        dto.add(linkTo(methodOn(PaymentController.class).getPaymentById(dto.getId())).withSelfRel());

        // 2. Statü güncelleme linki (Update)
        dto.add(linkTo(methodOn(PaymentController.class).updatePaymentStatus(dto.getId(), null))
                .withRel("update-status"));
        
        // 3. Workflow gereği: İlgili Request'e (Yardım Talebi) gidiş linki (Dış Link)
        // Not: Bu link Dispatcher üzerinden Requests servisine yönlenir.
        dto.add(linkTo(methodOn(PaymentController.class).createPayment(null))
                .slash("requests").slash(dto.getRequestId()).withRel("related-request"));

        return dto;
    }
}
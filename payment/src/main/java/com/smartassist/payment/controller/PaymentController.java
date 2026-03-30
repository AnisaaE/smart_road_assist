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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO request) {
        // 1. Servisi çağır ve DTO'yu al
        PaymentResponseDTO response = paymentService.createPayment(request);

        // 2. HATEOAS Linklerini Ekle (Self Link)
        response.add(linkTo(methodOn(PaymentController.class).getPaymentById(response.getId())).withSelfRel());

        // 3. Ek Navigasyon Linki (Update Status)
        response.add(linkTo(methodOn(PaymentController.class).updatePaymentStatus(response.getId(), null))
                .withRel("update-status"));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Testlerin derlenmesi için boş metodlar (İçini Service aşamasında dolduracağız)
    @GetMapping("/{id}")
    public PaymentResponseDTO getPaymentById(@PathVariable String id) { return null; }

    @PatchMapping("/{id}/status")
    public PaymentResponseDTO updatePaymentStatus(@PathVariable String id, @RequestParam String value) { return null; }
}
package com.smartassist.payment.controller;

import com.smartassist.payment.exception.InvalidPaymentStatusException;
import com.smartassist.payment.service.IPaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerUpdateStatusTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPaymentService paymentService;

    @Test
    @DisplayName("Zaten ödenmiş bir kaydı tekrar güncellemeye çalışınca 409 Conflict dönmeli")
    void shouldReturn409WhenStatusIsTerminal() throws Exception {
        // GIVEN
        Mockito.when(paymentService.updatePaymentStatus("pay-123", "FAILED"))
                .thenThrow(new InvalidPaymentStatusException("Terminal state cannot be changed"));

        // WHEN & THEN
        mockMvc.perform(patch("/payments/pay-123/status")
                        .param("value", "FAILED"))
                .andExpect(status().isConflict()); // 409 Bekliyoruz
    }
}
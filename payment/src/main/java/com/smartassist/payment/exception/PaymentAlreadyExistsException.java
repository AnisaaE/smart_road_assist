package com.smartassist.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Bu anotasyon, hata fırlatıldığında otomatik olarak 409 Conflict dönmesini sağlar
@ResponseStatus(HttpStatus.CONFLICT) 
public class PaymentAlreadyExistsException extends RuntimeException {
    public PaymentAlreadyExistsException(String message) {
        super(message);
    }
}
package com.smartassist.notification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Zaten READ olan bir bildirimi tekrar okumaya çalışınca fırlatılır.
 * HTTP 409 Conflict döner.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyReadException extends RuntimeException {
    public AlreadyReadException(String message) {
        super(message);
    }
}
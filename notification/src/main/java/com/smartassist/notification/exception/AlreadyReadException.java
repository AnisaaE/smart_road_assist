package com.smartassist.notification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyReadException extends RuntimeException {
    public AlreadyReadException(String message) {
        super(message);
    }
}
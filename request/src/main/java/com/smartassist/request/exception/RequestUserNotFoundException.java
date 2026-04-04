package com.smartassist.request.exception;

public class RequestUserNotFoundException extends RuntimeException {

    public RequestUserNotFoundException(String message) {
        super(message);
    }
}

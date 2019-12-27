package com.vouchers.exception;

public class ApplicationException extends RuntimeException {

    private final String message;

    ApplicationException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

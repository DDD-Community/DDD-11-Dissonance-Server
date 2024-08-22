package com.dissonance.itit.common.exception;

public record ErrorResponse(
        String errorCode,
        String message
) {
    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}

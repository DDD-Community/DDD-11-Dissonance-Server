package com.dissonance.itit.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NON_EXISTENT_EMAIL(HttpStatus.NOT_FOUND, "해당 email의 사용자가 존재하지 않습니다."),
    INVALID_PROVIDER(HttpStatus.BAD_REQUEST, "존재하지 않는 provider입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
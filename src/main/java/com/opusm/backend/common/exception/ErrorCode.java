package com.opusm.backend.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    TEST("test.error.message");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}

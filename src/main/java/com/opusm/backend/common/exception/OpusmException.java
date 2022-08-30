package com.opusm.backend.common.exception;

public class OpusmException extends RuntimeException {
    public OpusmException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public OpusmException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
    }

    public static void validate(boolean expression, ErrorCode errorCode) {
        if (!expression) {
            throw new OpusmException(errorCode);
        }
    }
}

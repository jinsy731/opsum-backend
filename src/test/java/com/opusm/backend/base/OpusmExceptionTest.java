package com.opusm.backend.base;


import com.opusm.backend.common.exception.ErrorCode;
import com.opusm.backend.common.exception.CustomException;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableTypeAssert;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OpusmExceptionTest {
    private final ErrorCode errorCode;
    private final ThrowableTypeAssert<CustomException> throwableTypeAssert;

    public OpusmExceptionTest(ErrorCode errorCode, ThrowableTypeAssert<CustomException> throwableTypeAssert) {
        this.errorCode = errorCode;
        this.throwableTypeAssert = throwableTypeAssert;
    }

    public static OpusmExceptionTest assertThatOpusmException(ErrorCode errorCode) {
        return new OpusmExceptionTest(errorCode, assertThatExceptionOfType(CustomException.class));
    }

    public void isThrownBy(ThrowableAssert.ThrowingCallable throwingCallable) {
        throwableTypeAssert.isThrownBy(throwingCallable).withMessage(errorCode.getMessage());
    }
}

package com.opusm.backend.base;


import com.opusm.backend.common.exception.ErrorCode;
import com.opusm.backend.common.exception.OpusmException;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableTypeAssert;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OpusmExceptionTest {
    private final ErrorCode errorCode;
    private final ThrowableTypeAssert<OpusmException> throwableTypeAssert;

    public OpusmExceptionTest(ErrorCode errorCode, ThrowableTypeAssert<OpusmException> throwableTypeAssert) {
        this.errorCode = errorCode;
        this.throwableTypeAssert = throwableTypeAssert;
    }

    public static OpusmExceptionTest assertThatOpusmException(ErrorCode errorCode) {
        return new OpusmExceptionTest(errorCode, assertThatExceptionOfType(OpusmException.class));
    }

    public void isThrownBy(ThrowableAssert.ThrowingCallable throwingCallable) {
        throwableTypeAssert.isThrownBy(throwingCallable).withMessage(errorCode.getMessage());
    }
}

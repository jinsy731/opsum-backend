package com.opusm.backend.api.advice;

import com.opusm.backend.common.exception.OpusmException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.NoSuchElementException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApiAdvice {
    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ErrorResponse exception(Exception ex) {
        log.info(ex.getMessage(), ex);

        return new ErrorResponse(new ErrorData("예상치 못한 에러가 발생하였습니다."));
    }

    @ExceptionHandler(OpusmException.class)
    public ErrorResponse OpusmException(Exception ex) {
        log.info(ex.getMessage(), ex);

        String message = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());

        return new ErrorResponse(new ErrorData(message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse argumentException(Exception ex) {
        log.info(ex.getMessage(), ex);
        return new ErrorResponse(new ErrorData(ex.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorResponse methodException(Exception ex) {
        log.info(ex.getMessage(), ex);
        return new ErrorResponse(new ErrorData(ex.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse elementNotFoundException(Exception ex) {
        log.info(ex.getMessage(), ex);
        return new ErrorResponse(new ErrorData("해당하는 자원을 찾을 수 없습니다."));
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        ErrorData error;
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorData {
        String message;
    }
}

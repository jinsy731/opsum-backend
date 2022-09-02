package com.opusm.backend.api.controller;

import com.opusm.backend.common.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApiAdvice {
    private final MessageSource messageSource;

    @ExceptionHandler(CustomException.class)
    public ErrorResponse customException(CustomException ex) {
        log.info(ex.getMessage(), ex);

        val message = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());

        return new ErrorResponse(new ErrorData(message));
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse exception(Exception ex) {
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

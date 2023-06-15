package com.ceaa.jigsawlibrary.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Slf4j
@ControllerAdvice
public class ValidationExceptionsAdvice {
    public static final String ERROR_MESSAGE = "Validation failed for request";

    @ResponseBody
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Mono<Error> requestParameterValidationExceptionsHandler(WebExchangeBindException exception) {
        HashMap<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });
        log.error("Validation failed for request parameter on fields: {}", errors, exception);
        return Mono.just(new Error(ErrorCode.BAD_REQUEST, ERROR_MESSAGE, errors));
    }
}

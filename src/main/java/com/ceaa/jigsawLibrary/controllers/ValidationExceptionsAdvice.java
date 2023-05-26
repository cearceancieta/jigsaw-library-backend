package com.ceaa.jigsawLibrary.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

@Slf4j
@ControllerAdvice
public class ValidationExceptionsAdvice {

    public static final String ERROR_MESSAGE_TEMPLATE = "Validation failed for RequestEntity [{}] in Method [{} -> {}]: {}";

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    HashMap<String, String> validationExceptionsHandler(MethodArgumentNotValidException exception) {
        HashMap<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });
        log.error(ERROR_MESSAGE_TEMPLATE,
                exception.getParameter().getParameter().getType().getName(),
                exception.getParameter().getContainingClass().getName(),
                exception.getParameter().getMethod().getName(),
                errors, exception);
        return errors;
    }

}

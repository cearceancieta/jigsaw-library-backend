package com.ceaa.jigsawlibrary.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidationExceptionAdviceTest {

    @Test
    void handleParameterValidationException() {
        WebExchangeBindException exception = new StubWebExchangeBindException(
                mock(MethodParameter.class),
                mock(BindException.class));
        List<ObjectError> allErrors = new ArrayList<>();
        allErrors.add(new FieldError("object1", "field1", "message1"));
        allErrors.add(new FieldError("object2", "field2", "message2"));
        when(exception.getBindingResult().getAllErrors()).thenReturn(allErrors);

        ValidationExceptionsAdvice advice = new ValidationExceptionsAdvice();
        HashMap<String, String> errors = advice.requestParameterValidationExceptionsHandler(exception).block();

        assertThat(errors).hasSameSizeAs(allErrors);
        allErrors.forEach(objectError -> {
            FieldError fieldError = (FieldError) objectError;
            assertThat(errors.containsKey(fieldError.getField())).isTrue();
            assertThat(errors.get(fieldError.getField())).isEqualTo(fieldError.getDefaultMessage());
        });
    }

    private class StubWebExchangeBindException extends WebExchangeBindException {
        public StubWebExchangeBindException(MethodParameter parameter, BindingResult bindingResult) {
            super(parameter, bindingResult);
        }

        @Override
        public String getMessage() {
            return "Error message and stack trace";
        }
    }
}

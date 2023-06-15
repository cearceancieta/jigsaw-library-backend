package com.ceaa.jigsawlibrary.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidationExceptionAdviceTest {

    private ValidationExceptionsAdvice advice;

    @BeforeEach
    void setUp() {
        advice = new ValidationExceptionsAdvice();
    }

    @Test
    void handleRequestBodyValidationException() {
        WebExchangeBindException exception = new StubWebExchangeBindException(
                mock(MethodParameter.class),
                mock(BindException.class));
        List<ObjectError> allErrors = new ArrayList<>();
        allErrors.add(new FieldError("object1", "field1", "message1"));
        allErrors.add(new FieldError("object2", "field2", "message2"));
        when(exception.getBindingResult().getAllErrors()).thenReturn(allErrors);

        Error error = advice.requestBodyValidationExceptionHandler(exception).block();

        assertThat(error).isNotNull();
        assertThat(error.code()).isEqualTo(ErrorCode.BAD_REQUEST);
        assertThat(error.message()).isEqualTo(ValidationExceptionsAdvice.ERROR_MESSAGE);
        assertThat(error.details()).isNotNull();
        assertThat(error.details()).hasSameSizeAs(allErrors);
        allErrors.forEach(objectError -> {
            FieldError fieldError = (FieldError) objectError;
            assertThat(error.details().containsKey(fieldError.getField())).isTrue();
            assertThat(error.details().get(fieldError.getField())).isEqualTo(fieldError.getDefaultMessage());
        });
    }

    @Test
    void handlePathVariableValidationException() {
        String validationMessage = "must be valid param";
        ConstraintViolation violation = mock(ConstraintViolation.class);
        HashSet<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);
        ConstraintViolationException exception = new ConstraintViolationException(violations);
        when(violation.getPropertyPath()).thenReturn(PathImpl.createPathFromString("method.param"));
        when(violation.getMessage()).thenReturn(validationMessage);

        Error error = advice.pathVariableValidationExceptionHandler(exception).block();

        assertThat(error).isNotNull();
        assertThat(error.code()).isEqualTo(ErrorCode.BAD_REQUEST);
        assertThat(error.message()).isEqualTo(ValidationExceptionsAdvice.ERROR_MESSAGE);
        assertThat(error.details()).isNotNull();
        assertThat(error.details()).hasSize(1);
        assertThat(error.details()).containsEntry("param", validationMessage);
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

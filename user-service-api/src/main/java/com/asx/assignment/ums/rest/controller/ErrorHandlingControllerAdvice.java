package com.asx.assignment.ums.rest.controller;

import com.asx.assignment.ums.rest.dto.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        List<ValidationErrorResponse.Violation> violations = e.getConstraintViolations()
                .stream()
                .map(violation -> new ValidationErrorResponse.Violation(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ValidationErrorResponse.Violation> violations = e.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationErrorResponse.Violation(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        ValidationErrorResponse error = new ValidationErrorResponse();
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        /*List<ValidationErrorResponse.Violation> violations = e..getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationErrorResponse.Violation(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        ValidationErrorResponse error = new ValidationErrorResponse();*/
        e.printStackTrace();;
        return new ValidationErrorResponse(null);
    }
}

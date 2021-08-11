package com.myproject.myweb.exception.dto;

import org.springframework.validation.BindingResult;

public class FieldError {
    private String field;
    private Object rejectedValue;
    private String reason;

    public FieldError(org.springframework.validation.FieldError fieldError){
        this.field = fieldError.getField();
        this.rejectedValue = fieldError.getRejectedValue();
        this.reason = fieldError.getDefaultMessage();
    }
}

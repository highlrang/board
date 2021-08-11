package com.myproject.myweb.exception.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private List<FieldError> fieldErrors;
    private String code;

    @Builder
    public ErrorResponse(String message, ErrorCode errorCode){
        this.message = message;
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
    }

    @Builder
    public ErrorResponse(ErrorCode errorCode){
        message = errorCode.getMessage();
        status = errorCode.getStatus();
        code = errorCode.getCode();
    }

    public void addFieldErrors(List<FieldError> errors){ fieldErrors = errors;
    }
}

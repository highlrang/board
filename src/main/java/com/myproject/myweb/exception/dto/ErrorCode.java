package com.myproject.myweb.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    BAD_REQUEST(400, "C001", "Bad Request. There is incorrect data type."),
    ACCESS_DENIED(403, "C002", "Access denied. Check the authorization"),
    RESOURCE_NOT_FOUND(404, "C003", "There is no appropriate resourse. Check the request is valid"),
    METHOD_NOT_ALLOWED(405, "C004", "Method not allowed. Check the method type"),

    INTERNAL_SERVER_ERROR(500, "C005", "Server error");

    private final int status;
    private final String code;
    private final String message;

}

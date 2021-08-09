package com.myproject.myweb.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.myproject.myweb.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController //
@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    // return ResponseEntity.badRequest().body(e.getMessage());
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    // return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ErrorResponse> exception(JsonParseException exception){
        ErrorResponse errorResponse = new ErrorResponse();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        // ResponseEntity 에러 처리 서치하기
        // 글고 ErrorResponse가 필수??
        // FieldError, ErrorCode 객체 따로 생성?(code, status, msg)
    }
}

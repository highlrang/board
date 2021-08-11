package com.myproject.myweb.handler;

import com.myproject.myweb.exception.ArgumentException;
import com.myproject.myweb.exception.dto.ErrorCode;
import com.myproject.myweb.exception.dto.ErrorResponse;
import com.myproject.myweb.exception.dto.FieldError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @Autowired private MessageSource messageSource;

    /* Controller에서 다룸
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindException(BindException exception){
        ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST);
        List<FieldError> fieldErrors =
                exception.getFieldErrors().stream()
                            .map(FieldError::new)
                            .collect(Collectors.toList());
        response.addFieldErrors(fieldErrors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    */


    @ExceptionHandler(ArgumentException.class)
    public ResponseEntity<ErrorResponse> argumentException(ArgumentException e){
        String message = messageSource.getMessage(e.getMessage(), new String[]{e.getArg()}, Locale.getDefault());
        log.info("argument 에러 발생 - responseEntity 반환. 메세지 = " + message);
        ErrorResponse response = new ErrorResponse(message, ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

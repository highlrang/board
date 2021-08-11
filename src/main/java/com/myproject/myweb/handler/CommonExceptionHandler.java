package com.myproject.myweb.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.myproject.myweb.exception.dto.ErrorCode;
import com.myproject.myweb.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @Autowired private MessageSource messageSource;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 응답 status 설정 가능
    @ExceptionHandler({IllegalStateException.class})
    public String BadRequestException(IllegalStateException e, Model model) {
        String errorMessage;
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if(e.getMessage().isEmpty()) {
            errorMessage = messageSource.getMessage("UnknownError", new String[]{now}, Locale.getDefault());
        }else {
            errorMessage = messageSource.getMessage(e.getMessage(), null, Locale.getDefault());
        }

        ErrorResponse errorResponse = new ErrorResponse(errorMessage, ErrorCode.INTERNAL_SERVER_ERROR);
        model.addAttribute("errorResponse", errorResponse);
        log.error("error = " + e.getMessage() + " errorMessage = " + errorMessage + " time = " + now);

        return "error/error";
    }

}

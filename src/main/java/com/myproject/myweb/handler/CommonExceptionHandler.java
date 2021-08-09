package com.myproject.myweb.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.myproject.myweb.dto.ErrorResponse;
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


@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @Autowired private MessageSource messageSource;

    @ExceptionHandler({IllegalStateException.class})
    public String BadRequestException(IllegalStateException e, Model model) { // exception 변수에 final 붙이는 이유
        String errorMessage;
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if(e.getMessage().isEmpty()) {
            errorMessage = messageSource.getMessage("UnknownError", new String[]{now}, null);
        }else {
            errorMessage = messageSource.getMessage(e.getMessage(), null, null);
        }

        model.addAttribute("error", errorMessage);
        log.error("error = " + e.getMessage() + " errorMessage = " + errorMessage + "time = " + now);

        return "error/error";
    }

    // @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    // 지원하지 않는 HttpMethod - HttpRequestMethodNotSupportedException
    // AccessDeniedException

}

package com.myproject.myweb.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler({IllegalStateException.class})
    public String BadRequestException(IllegalStateException e, Model model) { // exception 변수에 final 붙이는 이유

        model.addAttribute("error", e.getMessage());
        log.warn("error", e.getMessage());

        return "error";
    }

    // api exception return json >> ResponseEntity<Object> ..
    // return ResponseEntity.badRequest().body(e.getMessage());
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    // return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

}

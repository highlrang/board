package com.myproject.myweb.exception;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ArgumentException extends RuntimeException{

    private Map<String, List<String>> messagesMap;

    public ArgumentException(Map<String, List<String>> messagesAndArgs){
        this.messagesMap = messagesAndArgs;
    }

    public ArgumentException(String message){
        super(message);
    }
}

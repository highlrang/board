package com.myproject.myweb.exception;

import lombok.Getter;

@Getter
public class ArgumentException extends RuntimeException{

    private String arg;

    public ArgumentException(){
        super();
    }

    public ArgumentException(String message){
        super(message);
    }

    public ArgumentException(String message, String arg){ // List<String> args ?
        super(message);
        this.arg = arg;
    }

}

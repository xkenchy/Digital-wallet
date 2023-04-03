package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotYourAccountException extends  RuntimeException{
    public NotYourAccountException(String message) {
        super(message);
    }
}

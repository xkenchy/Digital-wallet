package com.techelevator.tenmo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TransferInvalidDataException extends RuntimeException{
    public TransferInvalidDataException(String message) {
        super(message);
    }
}
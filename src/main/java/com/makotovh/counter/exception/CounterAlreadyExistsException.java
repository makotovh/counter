package com.makotovh.counter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CounterAlreadyExistsException extends RuntimeException {
    public CounterAlreadyExistsException() {
        super("Counter already exists");
    }
}

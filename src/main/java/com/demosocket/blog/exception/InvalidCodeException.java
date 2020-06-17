package com.demosocket.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InvalidCodeException extends RuntimeException {

    private static final long serialVersionUID = -3313725120855823924L;
}

package com.demosocket.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserAlreadyEnabledException extends RuntimeException {

    private static final long serialVersionUID = -1816250246065092131L;
}

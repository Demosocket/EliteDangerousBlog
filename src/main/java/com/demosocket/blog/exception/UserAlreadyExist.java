package com.demosocket.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserAlreadyExist extends RuntimeException {

    private static final long serialVersionUID = -285433284280156277L;
}

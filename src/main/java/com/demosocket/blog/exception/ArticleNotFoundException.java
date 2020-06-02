package com.demosocket.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ArticleNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 4986141038931211875L;
}

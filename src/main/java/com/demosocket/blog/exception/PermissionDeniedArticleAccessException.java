package com.demosocket.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class PermissionDeniedArticleAccessException extends RuntimeException {

    private static final long serialVersionUID = 6047912913154598823L;
}

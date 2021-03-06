package com.demosocket.blog.handler;

import com.demosocket.blog.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<?> handleInvalidCode(final InvalidCodeException e, final WebRequest request) {
        final String bodyOfResponse = "Your code is no longer valid";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(UserAlreadyExist.class)
    public ResponseEntity<?> handleUserAlreadyExist(final UserAlreadyExist e, final WebRequest request) {
        final String bodyOfResponse = "User with such email already exist";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UserAlreadyEnabledException.class)
    public ResponseEntity<?> handleUserAlreadyEnabled(final UserAlreadyEnabledException e, final WebRequest request) {
        final String bodyOfResponse = "User with such email already enabled";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<?> handleArticleNotFound(final ArticleNotFoundException e, final WebRequest request) {
        final String bodyOfResponse = "Article with this id not found";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(PermissionDeniedArticleAccessException.class)
    public ResponseEntity<?> handleArticleNotFound(final PermissionDeniedArticleAccessException e,
                                                   final WebRequest request) {
        final String bodyOfResponse = "You don't have permissions to do something with this article";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(PermissionDeniedCommentAccessException.class)
    public ResponseEntity<?> handleArticleNotFound(final PermissionDeniedCommentAccessException e,
                                                   final WebRequest request) {
        final String bodyOfResponse = "You don't have permissions to do something with this comment";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(final UserNotFoundException e, final WebRequest request) {
        final String bodyOfResponse = "User not found";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<?> handleCommentNotFound(final CommentNotFoundException e, final WebRequest request) {
        final String bodyOfResponse = "Comment with this id not found";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<?> handleTagNotFound(final TagNotFoundException e, final WebRequest request) {
        final String bodyOfResponse = "Tag not found";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}

package com.demosocket.blog.security;

public class SecurityConstants {

    public static final String URL_AUTHENTICATE = "/authenticate";
    public static final String URL_REGISTRATION = "/auth/**";
    public static final String URL_ARTICLES = "/articles/**";
    public static final String UNAUTHORIZED_MESSAGE = "Wrong email or password";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String MESSAGE_CANT_GET_TOKEN = "Unable to get JWT Token";
    public static final String MESSAGE_TOKEN_HAS_EXPIRED = "JWT Token has expired";
}

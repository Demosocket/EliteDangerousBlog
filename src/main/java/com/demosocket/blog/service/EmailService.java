package com.demosocket.blog.service;

public interface EmailService {

    void sendRegistrationEmail(String email, String token);
}

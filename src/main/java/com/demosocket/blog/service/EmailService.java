package com.demosocket.blog.service;

public interface EmailService {

    void sendEmail(String email, String url, String token);
}

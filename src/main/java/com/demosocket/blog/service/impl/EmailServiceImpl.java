package com.demosocket.blog.service.impl;

import com.demosocket.blog.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String EMAIL;

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendRegistrationEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL);
        message.setTo(email);
        message.setText("http://localhost:8080/auth/confirm/" + token);
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

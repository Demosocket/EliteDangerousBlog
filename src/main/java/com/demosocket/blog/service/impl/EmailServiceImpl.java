package com.demosocket.blog.service.impl;

import com.demosocket.blog.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String EMAIL;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String email, String url, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL);
        message.setTo(email);
        message.setText(url + code);
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

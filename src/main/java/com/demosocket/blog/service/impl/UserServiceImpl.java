package com.demosocket.blog.service.impl;

import com.demosocket.blog.dto.UserRegisterDto;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Value("${spring.mail.username}")
    private String EMAIL;

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void register(UserRegisterDto userRegisterDto) {
//        send message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL);
        message.setTo(userRegisterDto.getEmail());
        message.setText("Hello " + userRegisterDto.getFirstName() + " "
                + userRegisterDto.getLastName() + " Spring Boot Application");
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        userRepository.save(userRegisterDto.toUser());
    }
}

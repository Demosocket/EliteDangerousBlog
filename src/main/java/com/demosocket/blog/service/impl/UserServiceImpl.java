package com.demosocket.blog.service.impl;

import com.demosocket.blog.dto.UserRegisterDto;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void register(UserRegisterDto userRegisterDto) {
        userRepository.save(userRegisterDto.toUser());
    }
}

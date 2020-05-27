package com.demosocket.blog.controller;

import com.demosocket.blog.dto.UserRegisterDto;
import com.demosocket.blog.model.User;
import com.demosocket.blog.repository.RedisRepository;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class RegistrationController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RedisRepository redisRepository;

    @Autowired
    public RegistrationController(UserService userService, UserRepository userRepository, RedisRepository redisRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.redisRepository = redisRepository;
    }

    @PostMapping("/sign_up")
    public ResponseEntity<?> signUp(@RequestBody UserRegisterDto userRegisterDto){
        User userFromDb = userRepository.findByEmail(userRegisterDto.getEmail());
        if (userFromDb != null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        userService.register(userRegisterDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.demosocket.blog.service.impl;

import com.demosocket.blog.dto.UserRegisterDto;
import com.demosocket.blog.model.User;
import com.demosocket.blog.repository.RedisRepository;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.security.jwt.JwtTokenUtil;
import com.demosocket.blog.service.EmailService;
import com.demosocket.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final EmailService emailService;
    private final RedisRepository redisRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                           JwtTokenUtil jwtTokenUtil,
                           EmailService emailService,
                           RedisRepository redisRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.emailService = emailService;
        this.redisRepository = redisRepository;
    }

    @Override
    public void registerNewUser(UserRegisterDto userRegisterDto) {
//        setHashPassword before saving in db
        userRegisterDto.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        userRepository.save(userRegisterDto.toEntity());
//        send email to
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userRegisterDto.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        emailService.sendRegistrationEmail(userRegisterDto.getEmail(), token);
        redisRepository.save(userRegisterDto.getEmail(), token);
    }

    @Override
    public void confirmEmail(String token) {
        final String emailFomToken = jwtTokenUtil.getEmailFromToken(token);
//        activate user
        User notActiveUser = userRepository.findByEmail(emailFomToken);
        notActiveUser.setEnabled(true);
        userRepository.save(notActiveUser);
//        delete data from Redis
        redisRepository.delete(emailFomToken);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

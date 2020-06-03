package com.demosocket.blog.service.impl;

import com.demosocket.blog.model.User;
import com.demosocket.blog.dto.UserRegisterDto;
import com.demosocket.blog.service.UserService;
import com.demosocket.blog.utils.CodeGenerator;
import com.demosocket.blog.service.EmailService;
import com.demosocket.blog.dto.UserResetPasswordDto;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.repository.RedisRepository;
import com.demosocket.blog.exception.InvalidCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final EmailService emailService;
    private final CodeGenerator codeGenerator;
    private final UserRepository userRepository;
    private final RedisRepository redisRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(EmailService emailService,
                           CodeGenerator codeGenerator,
                           UserRepository userRepository,
                           RedisRepository redisRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.emailService = emailService;
        this.codeGenerator = codeGenerator;
        this.userRepository = userRepository;
        this.redisRepository = redisRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static final String URL_CONFIRM_REGISTRATION = "http://localhost:8080/auth/confirm/";
    public static final String URL_RESTORE_PASSWORD = "";
    public static final String REDIS_KEY_FOR_HASH_CODE = "CONFIRM";
    public static final String REDIS_KEY_FOR_RESTORE_CODE = "RESTORE";

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void registerNewUser(UserRegisterDto userRegisterDto) {
//        setHashPassword before saving in db
        userRegisterDto.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        userRepository.save(userRegisterDto.toEntity());
        activateUser(userRegisterDto.getEmail());
    }

    @Override
    public void activateUser(String email) {
//        save registrationHashCode in redisRepository and send email
        String code = codeGenerator.randomHash();
        redisRepository.saveCode(REDIS_KEY_FOR_HASH_CODE, email, code);
        emailService.sendEmail(email, URL_CONFIRM_REGISTRATION, code);
    }

    @Override
    public void sendRestoreEmail(String email) {
        String code = codeGenerator.randomCode();
        redisRepository.saveCode(REDIS_KEY_FOR_RESTORE_CODE, email, code);
        emailService.sendEmail(email, URL_RESTORE_PASSWORD, code);
    }

    @Override
    public void confirmEmail(String registrationHashCode) {
        String email = (String) redisRepository.findAllCodes(REDIS_KEY_FOR_HASH_CODE).entrySet()
                .stream()
                .filter(entry -> registrationHashCode.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(InvalidCodeException::new);
//        activate user
        User notActiveUser = userRepository.findByEmail(email);
        notActiveUser.setEnabled(true);
        userRepository.save(notActiveUser);
//        delete data from redisDb
        redisRepository.deleteCode(REDIS_KEY_FOR_HASH_CODE, email);
    }

    @Override
    public void resetPassword(UserResetPasswordDto userResetPasswordDto) {
        String email = (String) redisRepository.findAllCodes(REDIS_KEY_FOR_RESTORE_CODE).entrySet()
                .stream()
                .filter(entry -> userResetPasswordDto.getCode().equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(InvalidCodeException::new);
//        reset password for user
        User userWithOldPassword = userRepository.findByEmail(email);
        userWithOldPassword.setHashPassword(passwordEncoder.encode(userResetPasswordDto.getPassword()));
        userRepository.save(userWithOldPassword);
//        delete data from redisDb
        redisRepository.deleteCode(REDIS_KEY_FOR_RESTORE_CODE, email);
    }
}

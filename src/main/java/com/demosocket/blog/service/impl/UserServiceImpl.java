package com.demosocket.blog.service.impl;

import com.demosocket.blog.dto.UserRegisterDto;
import com.demosocket.blog.dto.UserResetPasswordDto;
import com.demosocket.blog.model.User;
import com.demosocket.blog.repository.RedisRepository;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.service.EmailService;
import com.demosocket.blog.service.UserService;
import com.demosocket.blog.utils.RestoreCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RedisRepository redisRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           EmailService emailService,
                           RedisRepository redisRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.redisRepository = redisRepository;
    }

    public static final String URL_CONFIRM_REGISTRATION = "http://localhost:8080/auth/confirm/";
    public static final String URL_RESTORE_PASSWORD = "";
    public static final String REDIS_KEY_FOR_HASH_CODE = "CONFIRM";
    public static final String REDIS_KEY_FOR_RESTORE_CODE = "RESTORE";

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
        String code = UUID.randomUUID().toString();
        redisRepository.saveCode(REDIS_KEY_FOR_HASH_CODE, email, code);
        emailService.sendEmail(email, URL_CONFIRM_REGISTRATION, code);
    }

    @Override
    public void confirmEmail(String registrationHashCode) {
        Optional<Object> email = redisRepository.findAllCodes(REDIS_KEY_FOR_HASH_CODE).entrySet()
                .stream()
                .filter(entry -> registrationHashCode.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();
//        check if hash_code is valid
        if (email.isPresent()) {
//            activate user
            User notActiveUser = userRepository.findByEmail(email.get().toString());
            notActiveUser.setEnabled(true);
            userRepository.save(notActiveUser);
//            delete data from redisDb
            redisRepository.deleteCode(REDIS_KEY_FOR_HASH_CODE, email.get().toString());
        } else {
//            do something here when time your hash_code is out
            System.out.println("Hash_code is gone...");
        }
    }

    @Override
    public void sendRestoreEmail(String email) {
        String code = RestoreCodeGenerator.randomString();
        redisRepository.saveCode(REDIS_KEY_FOR_RESTORE_CODE, email, code);
        emailService.sendEmail(email, URL_RESTORE_PASSWORD, code);
    }

    @Override
    public void resetPassword(UserResetPasswordDto userResetPasswordDto) {
        Optional<Object> email = redisRepository.findAllCodes(REDIS_KEY_FOR_RESTORE_CODE).entrySet()
                .stream()
                .filter(entry -> userResetPasswordDto.getCode().equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();
//        check if code is valid
        if (email.isPresent()) {
//            reset password for user
            User userWithOldPassword = userRepository.findByEmail(email.get().toString());
            userWithOldPassword.setHashPassword(passwordEncoder.encode(userResetPasswordDto.getPassword()));
            userRepository.save(userWithOldPassword);
//            delete data from redisDb
            redisRepository.deleteCode(REDIS_KEY_FOR_RESTORE_CODE, email.get().toString());
        } else {
//            do something here when time your code is out
            System.out.println("Code do not found...");
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

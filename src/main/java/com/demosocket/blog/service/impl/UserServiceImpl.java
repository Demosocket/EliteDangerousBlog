package com.demosocket.blog.service.impl;

import com.demosocket.blog.model.RegistrationHashCode;
import com.demosocket.blog.dto.UserRegisterDto;
import com.demosocket.blog.model.User;
import com.demosocket.blog.repository.RedisRepository;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.service.EmailService;
import com.demosocket.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

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
        RegistrationHashCode registrationHashCode = new RegistrationHashCode(email);
        emailService.sendRegistrationEmail(email, registrationHashCode.getRegistrationHash());
        redisRepository.save(registrationHashCode);
    }

    @Override
    public void confirmEmail(String registrationHashCode) {
        RegistrationHashCode registrationHashCodeFromDb = null;
//        find all hash_codes in redisDb
        Map<String, RegistrationHashCode> mapOfRegistrationHashCodes = redisRepository.findAllRegistrationHashCodes();
        for (Map.Entry<String, RegistrationHashCode> entry : mapOfRegistrationHashCodes.entrySet()) {
            if (entry.getValue().getRegistrationHash().equals(registrationHashCode)) {
                registrationHashCodeFromDb = entry.getValue();
            }
        }
//        check if hash_code is valid
        if (registrationHashCodeFromDb != null && registrationHashCodeFromDb.checkExpiryDate()) {
//            activate user
            User notActiveUser = userRepository.findByEmail(registrationHashCodeFromDb.getEmail());
            notActiveUser.setEnabled(true);
            userRepository.save(notActiveUser);
//            delete data from redisDb
            redisRepository.delete(registrationHashCodeFromDb.getEmail());
        } else {
//            do something here - we don't send a response for client..
            System.out.println("Time is out, you should generate new code");
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

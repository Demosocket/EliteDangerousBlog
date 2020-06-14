package com.demosocket.blog.service.impl;

import com.demosocket.blog.exception.UserNotFoundException;
import com.demosocket.blog.model.User;
import com.demosocket.blog.repository.RedisRepository;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.service.EmailService;
import com.demosocket.blog.service.UserService;
import com.demosocket.blog.utils.CodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserServiceImpl.class)
class UserServiceImplTest {

    private static final String EMAIL = "email";
    private static User testUser;

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EmailService emailService;

    @MockBean
    private CodeGenerator codeGenerator;

    @MockBean
    private RedisRepository redisRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        testUser = User.builder().email(EMAIL).build();
    }

    @Test
    void shouldFindUserByEmail() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        assertEquals(testUser, userService.findByEmail(EMAIL));
    }

    @Test
    public void shouldThrowExceptionWhenEmailNotExists() {
        given(userRepository.findByEmail(anyString())).willThrow(new UserNotFoundException());
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(anyString()));
    }
}

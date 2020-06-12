package com.demosocket.blog.service.impl;

import com.demosocket.blog.model.User;
import com.demosocket.blog.model.UserRole;
import com.demosocket.blog.repository.RedisRepository;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.service.EmailService;
import com.demosocket.blog.service.UserService;
import com.demosocket.blog.utils.CodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = UserServiceImpl.class)
class UserServiceImplTest {

    public static final String EMAIL = "email";
    private static User testUser;

    @MockBean
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
    public void prepareTestData() {
        testUser = User.builder()
                .id(1)
                .firstName("Violet")
                .lastName("Evergarden")
                .email(EMAIL)
                .hashPassword("pass")
                .userRole(UserRole.USER)
//                .enabled(false)
                .build();
    }

    @Test
    void shouldFindUserByEmail() {
        given(userService.findByEmail(EMAIL)).willReturn(testUser);
        assertEquals(testUser, userService.findByEmail(EMAIL));
    }

    @Test
    public void shouldReturnNullWhenEmailNotExists() {
        given(userService.findByEmail(anyString())).willReturn(null);
        assertNull(userService.findByEmail(anyString()));
    }

//    @Test
//    void registerNewUser() {
//        UserNewDto userNewDto = UserNewDto.builder().email(EMAIL).firstName("Violet")
//                .lastName("Evergarden").password("pass").build();
//        userRepository.save(userNewDto.toEntity());
//        when
//    }

//    @Test
//    void activateUser() {
//
//    }

//    @Test
//    void sendRestoreEmail() {
//    }
//
//    @Test
//    void confirmEmail() {
//    }
//
//    @Test
//    void resetPassword() {
//    }
}
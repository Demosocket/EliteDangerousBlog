package com.demosocket.blog.service.impl;

import com.demosocket.blog.model.User;
import com.demosocket.blog.model.UserRole;
import com.demosocket.blog.service.UserService;
import com.demosocket.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    public static final String EMAIL = "email";
    private static User testUser;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void prepareTestData() {
        testUser = User.builder()
                .id(1)
                .firstName("Violet")
                .lastName("Evergarden")
                .email(EMAIL)
                .hashPassword("pass")
                .createdAt(new Date())
                .userRole(UserRole.USER)
                .enabled(false)
                .build();
    }

    @Test
    void shouldFindUserByEmail() {
        Mockito.when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        assertEquals(testUser, userService.findByEmail(EMAIL));
    }

    @Test
    public void shouldReturnNullWhenEmailNotExists() {
        Mockito.when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        assertNull(userService.findByEmail("EMAIL"));
    }

//    @Test
//    void registerNewUser() {
//        UserNewDto userNewDto = UserNewDto.builder().email(EMAIL).firstName("Violet")
//                .lastName("Evergarden").password("pass").build();
//        Mockito.when(userService.registerNewUser(userNewDto)).thenAnswer();
//        Mockito.when(userRepository.save(testUser));
//    }
//
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
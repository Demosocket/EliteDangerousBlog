//package com.demosocket.blog.service.impl;
//
//import com.demosocket.blog.model.User;
//import com.demosocket.blog.model.UserRole;
//import com.demosocket.blog.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.test.context.event.annotation.BeforeTestClass;
//
//import java.util.Date;
//
//import static org.mockito.BDDMockito.given;
//
//class UserServiceImplTest {
//
//    public static final String EMAIL = "email";
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    private static User testUser;
//
//    @BeforeTestClass
//    public void prepareTestData() {
//        testUser = User.builder()
//                .id(1)
//                .firstName("Violet")
//                .lastName("Evergarden")
//                .email("v.e@gmail.com")
//                .hashPassword("fdgshgsjh23")
//                .createdAt(new Date())
//                .userRole(UserRole.USER)
//                .enabled(true)
//                .build();
//    }
//
//    @Test
//    void shouldFindByEmail() {
//        User user1 = User.builder().email("v.e@gmail.com").build();
//        given(userRepository.findByEmail("v.e@gmail.com")).willReturn(testUser);
//
//    }
//
////    @Test
////    public void shouldReturnNullWhenEmailNotExists() {
////        assertThat(userService.findByEmail(EMAIL), is(equalTo(false)));
////
////        assertThat(userService.findByEmail(EMAIL), is(equalTo(null)));
////
////        verify(userRepository, atLeastOnce()).findByEmailIgnoreCase(Matchers.anyString());
////    }
//
//    @Test
//    void registerNewUser() {
//    }
//
//    @Test
//    void activateUser() {
//    }
//
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
//}
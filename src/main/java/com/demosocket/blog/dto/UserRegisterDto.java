package com.demosocket.blog.dto;

import com.demosocket.blog.model.User;
import com.demosocket.blog.model.UserRole;
import com.demosocket.blog.model.UserStatus;
import lombok.Data;

import java.util.Date;

@Data
public class UserRegisterDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User toUser() {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .hashPassword(password)
                .createdAt(new Date())
                .userRole(UserRole.USER)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }
}

package com.demosocket.blog.dto;

import lombok.Data;
import com.demosocket.blog.model.User;
import com.demosocket.blog.model.UserRole;

import java.util.Date;

@Data
public class UserNewDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User toEntity() {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .hashPassword(password)
                .createdAt(new Date())
                .userRole(UserRole.USER)
                .enabled(false)
                .build();
    }
}

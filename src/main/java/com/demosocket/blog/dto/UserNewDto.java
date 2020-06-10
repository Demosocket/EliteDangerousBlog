package com.demosocket.blog.dto;

import lombok.Data;
import lombok.Builder;
import com.demosocket.blog.model.User;
import com.demosocket.blog.model.UserRole;

@Data
@Builder
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
                .userRole(UserRole.USER)
                .enabled(false)
                .build();
    }
}

package com.demosocket.blog.dto;

import com.demosocket.blog.model.User;
import com.demosocket.blog.model.UserRole;
import lombok.Builder;
import lombok.Data;

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

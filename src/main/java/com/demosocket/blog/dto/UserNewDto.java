package com.demosocket.blog.dto;

import com.demosocket.blog.model.User;
import com.demosocket.blog.model.UserRole;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserNewDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
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

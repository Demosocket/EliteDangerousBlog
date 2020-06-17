package com.demosocket.blog.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserEmailDto {

    @Email
    @NotBlank
    private String email;
}

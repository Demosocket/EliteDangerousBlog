package com.demosocket.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserResetPasswordDto {

    @NotBlank
    private String code;

    @NotBlank
    private String password;
}

package com.demosocket.blog.dto;

import lombok.Data;

@Data
public class UserResetPasswordDto {

    private String code;
    private String password;
}

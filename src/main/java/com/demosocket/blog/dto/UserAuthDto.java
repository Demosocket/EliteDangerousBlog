package com.demosocket.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserAuthDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}

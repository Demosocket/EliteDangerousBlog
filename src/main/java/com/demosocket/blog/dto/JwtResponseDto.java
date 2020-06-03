package com.demosocket.blog.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class JwtResponseDto {

    private final String jwtToken;
}

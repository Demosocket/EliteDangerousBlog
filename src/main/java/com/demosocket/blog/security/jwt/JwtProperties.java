package com.demosocket.blog.security.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "jwt.token")
@ConstructorBinding
@Getter
public class JwtProperties {

    private final String secret;
    private final Long expiration;

    public JwtProperties(String secret, Long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }
}

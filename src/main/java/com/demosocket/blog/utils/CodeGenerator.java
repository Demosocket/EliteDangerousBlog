package com.demosocket.blog.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.security.SecureRandom;

@Component
public class CodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int LENGTH = 8;

    public String randomCode() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++)
            sb.append(AB.charAt(RANDOM.nextInt(AB.length())));
        return sb.toString();
    }

    public String randomHash() {
        return UUID.randomUUID().toString();
    }
}

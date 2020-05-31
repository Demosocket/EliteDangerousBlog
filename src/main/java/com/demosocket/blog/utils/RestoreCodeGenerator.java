package com.demosocket.blog.utils;

import java.security.SecureRandom;

public class RestoreCodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int LENGTH = 8;

    public static String randomString() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++)
            sb.append(AB.charAt(RANDOM.nextInt(AB.length())));
        return sb.toString();
    }
}

package com.demosocket.blog.repository;

import java.util.Map;

public interface RedisRepository {

    void save(String email, String token);
    void delete(String email);
    String findByEmail(String email);
    Map<String, String> findAll();
}

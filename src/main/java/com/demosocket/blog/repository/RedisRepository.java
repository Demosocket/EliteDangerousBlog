package com.demosocket.blog.repository;

import java.util.Map;

public interface RedisRepository {

    void saveCode(String key, String email, String code);
    void deleteCode(String key, String email);
    Map<Object, Object> findAllCodes(String key);
}

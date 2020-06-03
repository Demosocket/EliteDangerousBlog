package com.demosocket.blog.repository.impl;

import org.springframework.stereotype.Repository;
import com.demosocket.blog.repository.RedisRepository;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveCode(String key, String email, String code) {
        redisTemplate.opsForHash().put(key, email, code);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
    }

    @Override
    public void deleteCode(String key, String email) {
        redisTemplate.opsForHash().delete(key, email);
    }

    @Override
    public Map<Object, Object> findAllCodes(String key) {
        return redisTemplate.opsForHash().entries(key);
    }
}

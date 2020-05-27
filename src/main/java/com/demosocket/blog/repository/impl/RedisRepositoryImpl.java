package com.demosocket.blog.repository.impl;

import com.demosocket.blog.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

    private static final String KEY = "TOKEN";

    private final RedisTemplate<String, String> redisTemplate;
    private HashOperations<String, String, String> hashOperations;

    public RedisRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(String email, String token) {
        hashOperations.put(KEY, email, token);
    }

    @Override
    public void delete(String email) {
        hashOperations.delete(KEY, email);
    }

    @Override
    public String findByEmail(String email) {
        return hashOperations.get(KEY, email);
    }

    @Override
    public Map<String, String> findAll() {
        return hashOperations.entries(KEY);
    }
}

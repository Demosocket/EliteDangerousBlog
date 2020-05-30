package com.demosocket.blog.repository.impl;

import com.demosocket.blog.model.RegistrationHashCode;
import com.demosocket.blog.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

    private static final String KEY = "CONFIRM_EMAIL";

    private final RedisTemplate<String, RegistrationHashCode> redisTemplate;
    private HashOperations<String, String, RegistrationHashCode> hashOperations;

    public RedisRepositoryImpl(RedisTemplate<String, RegistrationHashCode> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(RegistrationHashCode registrationHashCode) {
        hashOperations.put(KEY, registrationHashCode.getEmail(), registrationHashCode);
    }

    @Override
    public void delete(String email) {
        hashOperations.delete(KEY, email);
    }

    @Override
    public Map<String, RegistrationHashCode> findAllRegistrationHashCodes() {
        return hashOperations.entries(KEY);
    }
}

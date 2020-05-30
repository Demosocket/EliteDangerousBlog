package com.demosocket.blog.repository;

import com.demosocket.blog.model.RegistrationHashCode;

import java.util.Map;

public interface RedisRepository {

    void save(RegistrationHashCode registrationHashCode);
    void delete(String email);
    Map<String, RegistrationHashCode> findAllRegistrationHashCodes();
}

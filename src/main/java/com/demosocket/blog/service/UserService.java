package com.demosocket.blog.service;

import com.demosocket.blog.model.User;
import com.demosocket.blog.dto.UserRegisterDto;
import com.demosocket.blog.dto.UserResetPasswordDto;

public interface UserService {

    User findByEmail(String email);
    void activateUser(String email);
    void confirmEmail(String token);
    void sendRestoreEmail(String email);
    void registerNewUser(UserRegisterDto userRegisterDto);
    void resetPassword(UserResetPasswordDto userResetPasswordDto);
}

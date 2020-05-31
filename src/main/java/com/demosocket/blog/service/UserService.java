package com.demosocket.blog.service;

import com.demosocket.blog.dto.UserRegisterDto;
import com.demosocket.blog.dto.UserResetPasswordDto;
import com.demosocket.blog.model.User;

public interface UserService {

    void registerNewUser(UserRegisterDto userRegisterDto);
    void activateUser(String email);
    void confirmEmail(String token);
    void sendRestoreEmail(String email);
    void resetPassword(UserResetPasswordDto userResetPasswordDto);
    User findByEmail(String email);
}

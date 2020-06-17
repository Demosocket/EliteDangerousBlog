package com.demosocket.blog.service;

import com.demosocket.blog.dto.UserEmailDto;
import com.demosocket.blog.dto.UserNewDto;
import com.demosocket.blog.dto.UserResetPasswordDto;
import com.demosocket.blog.model.User;

public interface UserService {

    User findByEmail(String email);

    void activateUser(String email);

    void confirmEmail(String token);

    void sendRestoreEmail(String email);

    void sendAgain(UserEmailDto userEmailDto);

    void registerNewUser(UserNewDto userNewDto);

    void resetPassword(UserResetPasswordDto userResetPasswordDto);
}

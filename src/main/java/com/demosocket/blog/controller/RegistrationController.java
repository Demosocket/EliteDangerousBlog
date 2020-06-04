package com.demosocket.blog.controller;

import com.demosocket.blog.model.User;
import com.demosocket.blog.dto.UserNewDto;
import com.demosocket.blog.dto.UserEmailDto;
import com.demosocket.blog.service.UserService;
import com.demosocket.blog.dto.UserResetPasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign_up")
    public ResponseEntity<?> signUp(@RequestBody UserNewDto userNewDto) {
        userService.registerNewUser(userNewDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/send_again")
    public ResponseEntity<?> sendAgain(@RequestBody UserEmailDto userEmailDto) {
        User userFromDb = userService.findByEmail(userEmailDto.getEmail());
        if (userFromDb == null || userFromDb.isEnabled()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.activateUser(userEmailDto.getEmail());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/confirm/{hash_code}")
    public ResponseEntity<?> confirmEmail(@PathVariable(name = "hash_code") String registrationHashCode) {
        userService.confirmEmail(registrationHashCode);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestBody UserEmailDto userEmailDto) {
        userService.sendRestoreEmail(userEmailDto.getEmail());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody UserResetPasswordDto userResetPasswordDto) {
        userService.resetPassword(userResetPasswordDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

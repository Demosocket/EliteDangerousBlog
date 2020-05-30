package com.demosocket.blog.controller;

import com.demosocket.blog.dto.UserEmailDto;
import com.demosocket.blog.dto.UserRegisterDto;
import com.demosocket.blog.model.User;
import com.demosocket.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/sign_up", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signUp(@RequestBody UserRegisterDto userRegisterDto){
        User userFromDb = userService.findByEmail(userRegisterDto.getEmail());
        if (userFromDb != null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        userService.registerNewUser(userRegisterDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/send_again", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendAgain(@RequestBody UserEmailDto userEmailDto){
        User userFromDb = userService.findByEmail(userEmailDto.getEmail());
        if (userFromDb == null || userFromDb.isEnabled()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        userService.activateUser(userEmailDto.getEmail());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/confirm/{hash_code}")
    public ResponseEntity<?> confirmEmail(@PathVariable(name = "hash_code") String registrationHashCode) {
        userService.confirmEmail(registrationHashCode);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

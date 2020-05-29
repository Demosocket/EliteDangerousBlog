package com.demosocket.blog.controller;

import com.demosocket.blog.dto.UserRegisterDto;
import com.demosocket.blog.model.User;
import com.demosocket.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign_up")
    public ResponseEntity<?> signUp(@RequestBody UserRegisterDto userRegisterDto){
        User userFromDb = userService.findByEmail(userRegisterDto.getEmail());
        if (userFromDb != null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        userService.registerNewUser(userRegisterDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/confirm/{token}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> confirmEmail(@PathVariable(name = "token") String token) {
        userService.confirmEmail(token);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

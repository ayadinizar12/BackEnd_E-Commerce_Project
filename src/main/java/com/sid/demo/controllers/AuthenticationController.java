package com.sid.demo.controllers;
import com.sid.demo.dtos.UserDTO;
import com.sid.demo.dtos.UserLoginDTO;
import com.sid.demo.exceptions.AppException;
import com.sid.demo.models.User;
import com.sid.demo.services.AuthenticationService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody UserDTO user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new AppException("All fields are required.", HttpStatus.BAD_REQUEST);
        }

        return authenticationService.register(user.getEmail(), user.getPassword());
    }

    @PostMapping("/login")
    public UserLoginDTO login(@Valid @RequestBody UserDTO user) {
        UserLoginDTO userLoginDTO = authenticationService.login(user.getEmail(), user.getPassword());

        if (userLoginDTO.getUser() == null) {
            throw new AppException("Invalid credentials.", HttpStatus.NOT_FOUND);
        }

        return userLoginDTO;
    }
}

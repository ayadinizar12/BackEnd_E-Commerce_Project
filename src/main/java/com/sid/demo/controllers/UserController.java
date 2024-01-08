package com.sid.demo.controllers;

import com.sid.demo.dtos.CartDTO;
import com.sid.demo.dtos.OrderDTO;
import com.sid.demo.dtos.PaymentDTO;
import com.sid.demo.dtos.ProductDTO;
import com.sid.demo.dtos.UserDTO;
import com.sid.demo.dtos.UserLoginDTO;
import com.sid.demo.exceptions.AppException;
import com.sid.demo.models.Order;
import com.sid.demo.models.Product;
import com.sid.demo.models.User;
import com.sid.demo.services.AuthenticationService;
import com.sid.demo.services.CartService;
import com.sid.demo.services.OrderService;
import com.sid.demo.services.PaymentService;
import com.sid.demo.services.ProductService;
import com.sid.demo.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/v1/user")
public class UserController {

    private UserService userService;

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId, Authentication authentication) {
        return userService.getUserById(userId, authentication);
    }

    @PutMapping("/update/{userId}")
    public User updateUserById(@PathVariable Long userId, @RequestBody UserDTO userDTO, Authentication authentication) {
        return userService.updateUserById(userId, userDTO, authentication);
    }
}

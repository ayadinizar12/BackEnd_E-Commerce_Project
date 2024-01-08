package com.sid.demo.controllers;

import com.sid.demo.dtos.CartDTO;
import com.sid.demo.dtos.UserDTO;
import com.sid.demo.dtos.UserLoginDTO;
import com.sid.demo.exceptions.AppException;
import com.sid.demo.models.User;
import com.sid.demo.services.AuthenticationService;
import com.sid.demo.services.CartService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/v1/cart")
public class CartController {

    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getCartByUserId(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        CartDTO cartDTO = cartService.getCartByUserId(userId);
        if (cartDTO != null) {
            response.put("cart", cartDTO);
            response.put("numberOfItemsInCart", cartService.getNumberOfItemsInCart(userId));
            return ResponseEntity.ok().body(response);
        } else {
            throw new AppException("User's cart not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{userId}/{productId}/{quantity}")
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable Long userId, @PathVariable Long productId, @PathVariable int quantity) {
        CartDTO cartDto = cartService.addItemToCart(userId, productId, quantity);
        return ResponseEntity.ok().body(cartDto);
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<CartDTO> removeItemFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        CartDTO cartDTO = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok().body(cartDTO);
    }
}

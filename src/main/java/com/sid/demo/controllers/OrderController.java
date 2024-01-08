package com.sid.demo.controllers;

import com.sid.demo.dtos.CartDTO;
import com.sid.demo.dtos.OrderDTO;
import com.sid.demo.dtos.PaymentDTO;
import com.sid.demo.dtos.UserDTO;
import com.sid.demo.dtos.UserLoginDTO;
import com.sid.demo.exceptions.AppException;
import com.sid.demo.models.Order;
import com.sid.demo.models.User;
import com.sid.demo.services.AuthenticationService;
import com.sid.demo.services.CartService;
import com.sid.demo.services.OrderService;
import com.sid.demo.services.PaymentService;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
   
	private OrderService orderService;
    private CartService cartService;
    private PaymentService paymentService;

    @GetMapping("/{userId}")
    public List<OrderDTO> getOrdersByUserId(@PathVariable Long userId, Authentication authentication) {
        return orderService.getOrdersByUserId(userId, authentication);
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<PaymentDTO> checkout(@PathVariable Long userId, Authentication authentication) throws StripeException {
        CartDTO cart = cartService.getCartByUserId(userId);
        BigDecimal totalPrice = cart.getTotalPrice();
        PaymentIntent paymentIntent = paymentService.createPaymentIntent(totalPrice);

        Order createdOrder = orderService.createOrderFromCart(cart, userId, authentication);

        cartService.clearCart(userId);

        PaymentDTO paymentDTO = new PaymentDTO(paymentIntent.getClientSecret(), totalPrice, "usd", createdOrder.getId());

        return ResponseEntity.ok().body(paymentDTO);
    }
}

package com.sid.demo.services;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.sid.demo.dtos.CartDTO;
import com.sid.demo.dtos.CartltemDTO;
import com.sid.demo.dtos.OrderDTO;
import com.sid.demo.models.Order;
import com.sid.demo.models.Orderltem;
import com.sid.demo.models.Product;
import com.sid.demo.models.User;
import com.sid.demo.repository.*;
import com.sid.demo.exceptions.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {

	private OrderRepo orderRepo;
    private UserRepo userRepo;
    private ProductRepo productRepo;

    public List<OrderDTO> getOrdersByUserId(Long userId, Authentication authentication) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));

        if (authentication == null || !user.getEmail().equals(authentication.getName())) {
            throw new AppException("Access denied.", HttpStatus.BAD_REQUEST);
        }

        List<Order> orders = orderRepo.findAllByUserId(userId);
        List<OrderDTO> orderDtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setTotal(order.getTotal());
            String dateCreatedStr = dateFormat.format(order.getDateCreated());
            orderDTO.setDateCreated(dateCreatedStr);
            orderDtos.add(orderDTO);
        }
        return orderDtos;
    }

    public Order createOrderFromCart(CartDTO cart, Long userId, Authentication authentication) {
        User user = userRepo.findById(userId).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (authentication == null || !user.getEmail().equals(authentication.getName())) {
            throw new AppException("Access denied.", HttpStatus.BAD_REQUEST);
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotal(cart.getTotalPrice());
        order.setDateCreated(new Date());
        List<Orderltem> orderltems = new ArrayList<>();
        for (CartltemDTO cartltem : cart.getCartltems()) {
            Orderltem orderltem = new Orderltem();
            Product product = productRepo.findById(cartltem.getProductId()).orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));
            orderltem.setProduct(product);
            orderltem.setQuantity(cartltem.getQuantity());
            orderltems.add(orderltem);
        }
        order.setOrderltems(orderltems);
        return orderRepo.save(order);
    }


}

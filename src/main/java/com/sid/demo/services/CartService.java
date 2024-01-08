package com.sid.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


import com.sid.demo.dtos.*;
import com.sid.demo.models.*;
import com.sid.demo.mappers.*;
import com.sid.demo.repository.*;
import com.sid.demo.exceptions.*;


@RequiredArgsConstructor
@Service
public class CartService {

    private CartRepo cartRepo;
    private CartltemRepo cartltemRepo;
    private UserRepo userRepo;
    private ProductRepo productRepo;

    public CartDTO getCartByUserId(Long userId) {
        Optional<Cart> userCart = cartRepo.findByUserId(userId);
        if (userCart.isPresent()) {
            Cart cart = userCart.get();

            CartDTO cartDTO = new CartDTO();
            cartDTO.setId(cart.getId());
            cartDTO.setUserId(cart.getUserId());

            //List<CartltemDTO> cartltemDTOs = getCartltemDTO(cart);

            List<CartltemDTO> cartltemDTOs=getCartltemDTO(cart);
            Map<Long, CartltemDTO> cartltemMap = new HashMap<>();

            for (CartltemDTO cartltemDTO : cartltemDTOs) {
                Long productId = cartltemDTO.getProductId();
                if (cartltemMap.containsKey(productId)) {
                    CartltemDTO existingItem = cartltemMap.get(productId);
                    existingItem.setQuantity(existingItem.getQuantity() + cartltemDTO.getQuantity());
                    existingItem.setSubTotal(existingItem.getSubTotal().add(cartltemDTO.getSubTotal()));
                } else {
                    cartltemMap.put(productId, cartltemDTO);
                }
            }

            List<CartltemDTO> consolidatedCartltems = new ArrayList<>(cartltemMap.values());
            cartDTO.setCartltems(consolidatedCartltems);

            for (CartltemDTO consolidatedCartltem : consolidatedCartltems) {
                Long productId = consolidatedCartltem.getProductId();
                Product product = productRepo.findById(productId)
                        .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

                ProductDTO productDTO = new ProductDTO();
                productDTO.setImgUrl(product.getImgUrl());
                consolidatedCartltem.setProduct(productDTO);
            }

            BigDecimal totalPrice = consolidatedCartltems.stream()
                    .map(CartltemDTO::getSubTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            cartDTO.setTotalPrice(totalPrice);

            return cartDTO;
        } else {
            return null;
        }
    }

    public int getNumberOfItemsInCart(Long userId) {
        Optional<Cart> userCart = cartRepo.findByUserId(userId);
        if (userCart.isPresent()) {
            Cart cart = userCart.get();
            return cart.getCartltems().stream()
                    .mapToInt(Cartltem::getQuantity)
                    .sum();
        } else {
            return 0;
        }
    }

    private static List<CartltemDTO> getCartltemDTO(Cart cart) {
        List<CartltemDTO> cartltemDTOs = new ArrayList<>();
        for (Cartltem cartltem : cart.getCartltems()) {
            CartltemDTO cartltemDTO = new CartltemDTO();
            cartltemDTO.setProductId(cartltem.getProductId());
            cartltemDTO.setProductName(cartltem.getProductName());
            cartltemDTO.setQuantity(cartltem.getQuantity());
            cartltemDTO.setPrice(cartltem.getPrice());

            cartltemDTO.setSubTotal(cartltem.getPrice().multiply(BigDecimal.valueOf(cartltem.getQuantity())));
            cartltemDTOs.add(cartltemDTO);
        }
        return cartltemDTOs;
    }

    public CartDTO addItemToCart(Long userId, Long productId, int quantity) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        BigDecimal itemPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        Optional<Cart> optionalCart = cartRepo.findByUserId(userId);
        Cart userCart = optionalCart.orElse(new Cart());
        if (optionalCart.isEmpty()) {
            userCart.setUserId(userId);
            cartRepo.save(userCart);
        }

        Cartltem newltem = new Cartltem();
        newltem.setProductId(product.getId());
        newltem.setProductName(product.getName());
        newltem.setQuantity(quantity);
        newltem.setPrice(itemPrice);
        newltem.setCart(userCart);
        newltem.setSubTotal(itemPrice);

        cartltemRepo.save(newltem);

        userCart.getCartltems().add(newltem);

        BigDecimal totalPrice = userCart.getCartltems().stream()
                .map(Cartltem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        userCart.setTotalPrice(totalPrice);
        cartRepo.save(userCart);

        List<CartltemDTO> cartltemDTOs = userCart.getCartltems().stream()
                .map(ltem -> {
                    CartltemDTO ltemDTO = new CartltemDTO();
                    ltemDTO.setProductId(ltem.getProductId());
                    ltemDTO.setProductName(ltem.getProductName());
                    ltemDTO.setQuantity(ltem.getQuantity());
                    ltemDTO.setPrice(ltem.getPrice());
                    ltemDTO.setSubTotal(ltem.getPrice().multiply(BigDecimal.valueOf(ltem.getQuantity()))
                            .setScale(2, RoundingMode.HALF_UP));
                    return ltemDTO;
                }).collect(Collectors.toList());

        return CartMapper.INSTANCE.cartToCartDTO(userCart, totalPrice, cartltemDTOs);
    }

    public CartDTO removeItemFromCart(Long userId, Long productId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        Cart userCart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new AppException("Cart not found", HttpStatus.NOT_FOUND));

        Cartltem cartltemToRemove = userCart.getCartltems().stream()
                .filter(ltem -> ltem.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new AppException("Cart item not found", HttpStatus.NOT_FOUND));

        userCart.getCartltems().remove(cartltemToRemove);
        cartltemRepo.delete(cartltemToRemove);

        BigDecimal totalPrice = userCart.getCartltems().stream()
                .map(Cartltem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        userCart.setTotalPrice(totalPrice);
        cartRepo.save(userCart);

        List<CartltemDTO> cartltemDTOs = getCartltemDTO(userCart);
        return CartMapper.INSTANCE.cartToCartDTO(userCart, totalPrice, cartltemDTOs);
    }

    public Cart getCartEntityByUserId(Long userId) {
        return cartRepo.findByUserId(userId).orElseThrow(() ->
                new AppException("Cart not found for user id: " + userId, HttpStatus.NOT_FOUND));
    }

    public void clearCart(Long userId) {
        Cart cart = getCartEntityByUserId(userId);
        if (cart != null) {
            cart.getCartltems().clear();
            cart.setTotalPrice(BigDecimal.ZERO);
            cartRepo.save(cart);
        }
    }
}

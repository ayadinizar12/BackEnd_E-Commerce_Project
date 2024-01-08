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

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/products")
public class ProductController {

    private ProductService productService;

    @GetMapping("all")
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    @PostMapping("add")
    public Product add(@RequestBody ProductDTO productDto) {
        if (
                productDto.getName() == null ||
                productDto.getName().isEmpty() ||
                productDto.getDescription() == null || productDto.getDescription().isEmpty() ||
                productDto.getImgUrl() == null || productDto.getImgUrl().isEmpty() ||
                productDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {

            throw new AppException("All fields are required.", HttpStatus.BAD_REQUEST);
        }
        Product newProduct = new Product();
        newProduct.setName(productDto.getName());
        newProduct.setDescription(productDto.getDescription());
        newProduct.setPrice(productDto.getPrice());
        newProduct.setImgUrl(productDto.getImgUrl());

        return productService.add(newProduct);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        Optional<Product> productOptional = productService.getProductById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return ResponseEntity.ok(product);
        } else {
            throw new AppException("Product not found", HttpStatus.NOT_FOUND);
        }
    }
}

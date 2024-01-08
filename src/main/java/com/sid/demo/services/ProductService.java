package com.sid.demo.services;

import com.sid.demo.dtos.*;
import com.sid.demo.models.*;
import com.sid.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private ProductRepo productRepo;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepo.findById(productId);
    }

    public Product add(Product product) {
        return productRepo.save(product);
    }
}

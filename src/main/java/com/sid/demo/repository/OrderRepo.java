package com.sid.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sid.demo.models.Order;
@Repository
public interface OrderRepo  extends JpaRepository<Order, Long>{
    List<Order> findAllByUserId(Long userId);
}

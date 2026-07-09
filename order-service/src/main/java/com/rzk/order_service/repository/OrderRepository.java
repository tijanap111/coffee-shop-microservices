package com.rzk.order_service.repository;

import com.rzk.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByCustomerId(Integer customerId);
    List<Order> findByStatus(String status);
}

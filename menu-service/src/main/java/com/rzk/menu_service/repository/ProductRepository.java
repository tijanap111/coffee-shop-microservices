package com.rzk.menu_service.repository;

import com.rzk.menu_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategoryId(Integer categoryId);
    List<Product> findByAvailableTrue();
    List<Product> findByNameContainingIgnoreCase(String name);
}

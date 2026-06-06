package com.rzk.menu_service.service;

import com.rzk.menu_service.model.Category;
import com.rzk.menu_service.model.Product;
import com.rzk.menu_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository pr;

    public List<Product> getAll() {
        return pr.findAll();
    }

    public Optional<Product> getById(Integer id) {
        return pr.findById(id);
    }

    public List<Product> getByCategory(Integer categoryId) {
        return pr.findByCategoryId(categoryId);
    }

    public List<Product> getAvailable() {
        return pr.findByAvailableTrue();
    }

    public List<Product> searchByName(String name) {
        return pr.findByNameContainingIgnoreCase(name);
    }

    public Product create(Product product) {
        return pr.save(product);
    }

    public Product update(Integer id, Product updated) {
        Product existing = pr.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: \" + id"));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setAvailable(updated.getAvailable());
        existing.setCategory(updated.getCategory());
        return pr.save(existing);
    }

    public void updateAvailability(Integer id, Product updated) {
        Product existing = pr.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: \" + id)"));
        existing.setAvailable(updated.getAvailable());
        pr.save(existing);
    }

    public void delete(Integer id) {
        pr.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: \" + id)"));
        pr.deleteById(id);
    }
}

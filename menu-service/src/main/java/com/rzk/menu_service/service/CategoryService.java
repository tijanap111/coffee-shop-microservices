package com.rzk.menu_service.service;

import com.rzk.menu_service.model.Category;
import com.rzk.menu_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository cr;

    public List<Category> getAll() {
        return cr.findAll();
    }

    public Optional<Category> getById(Integer id) {
        return cr.findById(id);
    }

    public Category create(Category category) {
        return cr.save(category);
    }

    public Category update(Integer id, Category updated) {
        Category existing = cr.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        return cr.save(existing);
    }

    public void delete(Integer id) {
        cr.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        cr.deleteById(id);
    }
}

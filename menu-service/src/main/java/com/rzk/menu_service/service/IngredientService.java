package com.rzk.menu_service.service;

import com.rzk.menu_service.model.Ingredient;
import com.rzk.menu_service.repository.IngredientRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ir;

    public List<Ingredient> getAll() {
        return ir.findAll();
    }

    public Optional<Ingredient> getById(Integer id) {
        return ir.findById(id);
    }

    public List<Ingredient> getAllergens() {
        return ir.findByIsAllergenTrue();
    }

    public List<Ingredient> getNonAllergens() {
        return ir.findByIsAllergenFalse();
    }

    public Ingredient create(Ingredient ingredient) {
        return ir.save(ingredient);
    }

    public Ingredient update(Integer id, Ingredient updated) {
        Ingredient existing = ir.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: \" + id)"));
        existing.setId(updated.getId());
        existing.setName(updated.getName());
        existing.setIsAllergen(updated.getIsAllergen());
        return ir.save(existing);
    }

    public void delete(Integer id) {
        ir.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: \" + id)"));
        ir.deleteById(id);
    }
}

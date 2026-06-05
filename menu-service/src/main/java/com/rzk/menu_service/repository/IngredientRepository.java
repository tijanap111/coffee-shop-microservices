package com.rzk.menu_service.repository;

import com.rzk.menu_service.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    List<Ingredient> findByIsAllergenTrue();
    List<Ingredient> findByIsAllergenFalse();
}

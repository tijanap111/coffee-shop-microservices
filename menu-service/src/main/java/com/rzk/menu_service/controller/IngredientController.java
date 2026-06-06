package com.rzk.menu_service.controller;

import com.rzk.menu_service.model.Ingredient;
import com.rzk.menu_service.repository.IngredientRepository;
import com.rzk.menu_service.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredients")
@Validated
public class IngredientController {

    private final IngredientService is;

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAll() {
        return new ResponseEntity<>(is.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getById(@PathVariable Integer id) {
        Optional<Ingredient> ingredient = is.getById(id);
        if (ingredient.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ingredient.get(), HttpStatus.OK);
    }

    @GetMapping("/allergens")
    public ResponseEntity<List<Ingredient>> getAllergens() {
        return new ResponseEntity<>(is.getAllergens(), HttpStatus.OK);
    }

    @GetMapping("/non-allergens")
    public ResponseEntity<List<Ingredient>> getNonAllergens() {
        return new ResponseEntity<>(is.getNonAllergens(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Ingredient> create(@RequestBody @Valid Ingredient ingredient) {
        return new ResponseEntity<>(is.create(ingredient), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> update(@PathVariable Integer id, @RequestBody @Valid Ingredient ingredient) {
        return new ResponseEntity<>(is.update(id, ingredient), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        is.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

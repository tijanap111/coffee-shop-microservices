package com.rzk.menu_service.controller;

import com.rzk.menu_service.model.Category;
import com.rzk.menu_service.repository.CategoryRepository;
import com.rzk.menu_service.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Validated
public class CategoryController {

    public final CategoryService cs;

    @GetMapping()
    public ResponseEntity<List<Category>> getAll() {
        return new ResponseEntity<>(cs.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Integer id) {
        Optional<Category> category = cs.getById(id);
        if (category.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(category.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody @Valid Category category) {
        return new ResponseEntity<>(cs.create(category), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Integer id, @RequestBody @Valid Category updated) {
        return new ResponseEntity<>(cs.update(id, updated), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        cs.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

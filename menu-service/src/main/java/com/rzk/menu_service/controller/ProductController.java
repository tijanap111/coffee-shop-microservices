package com.rzk.menu_service.controller;

import com.rzk.menu_service.model.Category;
import com.rzk.menu_service.model.Product;
import com.rzk.menu_service.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Validated
public class ProductController {

    private final ProductService ps;

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return new ResponseEntity<>(ps.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Integer id) {
        Optional<Product> product = ps.getById(id);
        if (product.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable Integer categoryId) {
        return new ResponseEntity<>(ps.getByCategory(categoryId), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Product>> getAvailable() {
        return new ResponseEntity<>(ps.getAvailable(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestParam @NotBlank String name) {
        return new ResponseEntity<>(ps.searchByName(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody @Valid Product product) {
        return new ResponseEntity<>(ps.create(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Integer id, @RequestBody @Valid Product updated) {
        return new ResponseEntity<>(ps.update(id, updated), HttpStatus.OK);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Void> updateAvailability(@PathVariable Integer id, @RequestBody @Valid Product updated) {
        ps.updateAvailability(id, updated);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            ps.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}

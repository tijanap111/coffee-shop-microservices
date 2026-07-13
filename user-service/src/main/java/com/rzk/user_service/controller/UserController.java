package com.rzk.user_service.controller;

import com.rzk.user_service.model.FavoriteProduct;
import com.rzk.user_service.model.LoyaltyCard;
import com.rzk.user_service.model.User;
import com.rzk.user_service.service.UserService;
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
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService us;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(us.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
        Optional<User> user = us.getById(id);
        if (user.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody @Valid User updated) {
        return new ResponseEntity<>(us.update(id, updated), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        us.getById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid User user) {
        return new ResponseEntity<>(us.register(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        try {
            String token = us.login(email, password);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}/loyalty")
    public ResponseEntity<LoyaltyCard> getLoyaltyCard(@PathVariable Integer id) {
        Optional<LoyaltyCard> card = us.getLCard(id);
        if (card.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(card.get(), HttpStatus.OK);
    }

    @PostMapping("/{id}/loyalty/add-points")
    public ResponseEntity<LoyaltyCard> addPoints(@PathVariable Integer id, @RequestParam Integer points) {
        return new ResponseEntity<>(us.addPoints(id, points), HttpStatus.OK);
    }

    @GetMapping("/{id}/favorites")
    public ResponseEntity<List<FavoriteProduct>> getFavorites(@PathVariable Integer id) {
        return new ResponseEntity<>(us.getFavorites(id), HttpStatus.OK);
    }

    @PostMapping("/{id}/favorites")
    public ResponseEntity<FavoriteProduct> addFavorite(@PathVariable Integer id, @RequestBody FavoriteProduct favoriteProduct) {
        return new ResponseEntity<>(us.addFavorite(id, favoriteProduct), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/favorites/{productId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Integer id, @PathVariable Integer productId) {
        us.removeFavorite(id, productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

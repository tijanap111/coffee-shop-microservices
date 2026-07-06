package com.rzk.user_service.service;

import com.rzk.user_service.model.FavoriteProduct;
import com.rzk.user_service.model.FavoriteProductId;
import com.rzk.user_service.model.LoyaltyCard;
import com.rzk.user_service.model.User;
import com.rzk.user_service.repository.FavoriteProductRepository;
import com.rzk.user_service.repository.LoyaltyCardRepository;
import com.rzk.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository ur;
    private final LoyaltyCardRepository lr;
    private final FavoriteProductRepository fr;

    public List<User> getAll() {
        return ur.findAll();
    }

    public Optional<User> getById(Integer id) {
        return ur.findById(id);
    }

    public User register(User user) {
        User savedUser = ur.save(user);

        LoyaltyCard card = new LoyaltyCard();
        card.setUser(savedUser);
        card.setLevel("BRONZE");
        card.setPoints(0);
        lr.save(card);

        return savedUser;
    }

    public User update(Integer id, User updated) {
        User existing = ur.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: \" + id"));

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setRole(updated.getRole());
        existing.setEmail(updated.getEmail());
        existing.setPassword(updated.getPassword());
        return ur.save(existing);
    }

    public void delete(Integer id) {
        ur.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found with id: \" + id)"));
        ur.deleteById(id);
    }

    public boolean login(String email, String password) {
        Optional<User> user = ur.findByEmail(email);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    public Optional<LoyaltyCard> getLCard(Integer userId) {
        return lr.findByUserId(userId);
    }

    @Transactional
    public LoyaltyCard addPoints(Integer userId, Integer points) {
        LoyaltyCard card = lr.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Loyalty card not found for user: " + userId));
        card.setPoints(card.getPoints() + points);

        if (card.getPoints() >= 500) card.setLevel("GOLD");
        else if (card.getPoints() >= 200) card.setLevel("SILVER");
        else card.setLevel("BRONZE");

        return lr.save(card);
    }

    public List<FavoriteProduct> getFavorites(Integer userId) {
        return fr.findByIdUserId(userId);
    }

    public FavoriteProduct addFavorite(Integer userId, FavoriteProduct favoriteProduct) {
        FavoriteProductId fpId = new FavoriteProductId();
        fpId.setUserId(userId);
        fpId.setProductId(favoriteProduct.getId().getProductId());
        favoriteProduct.setId(fpId);
        return fr.save(favoriteProduct);
    }

    public void removeFavorite(Integer userId, Integer productId) {
        FavoriteProductId fpId = new FavoriteProductId();
        fpId.setUserId(userId);
        fpId.setProductId(productId);
        fr.deleteById(fpId);
    }
}

package com.rzk.user_service.repository;

import com.rzk.user_service.model.LoyaltyCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoyaltyCardRepository extends JpaRepository<LoyaltyCard, Integer> {

    Optional<LoyaltyCard> findByUserId(Integer userId);
}

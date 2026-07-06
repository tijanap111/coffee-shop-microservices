package com.rzk.user_service.repository;

import com.rzk.user_service.model.FavoriteProduct;
import com.rzk.user_service.model.FavoriteProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, FavoriteProductId> {

    List<FavoriteProduct> findByIdUserId(Integer userId);
}

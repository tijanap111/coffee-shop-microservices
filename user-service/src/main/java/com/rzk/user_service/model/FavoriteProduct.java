package com.rzk.user_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "favorite_product")
public class FavoriteProduct {
    @EmbeddedId
    private FavoriteProductId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @jakarta.validation.constraints.Size(max = 150)
    @jakarta.validation.constraints.NotNull
    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;


}
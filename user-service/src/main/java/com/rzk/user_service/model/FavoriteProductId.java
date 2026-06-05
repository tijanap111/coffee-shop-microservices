package com.rzk.user_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class FavoriteProductId implements Serializable {
    private static final long serialVersionUID = -3195764434778253478L;
    @jakarta.validation.constraints.NotNull
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @jakarta.validation.constraints.NotNull
    @Column(name = "product_id", nullable = false)
    private Integer productId;


}
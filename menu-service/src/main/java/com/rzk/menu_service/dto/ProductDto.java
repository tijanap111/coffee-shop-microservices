package com.rzk.menu_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {

    private Integer id;
    private String name;
    private BigDecimal price;
    private Boolean available;
}

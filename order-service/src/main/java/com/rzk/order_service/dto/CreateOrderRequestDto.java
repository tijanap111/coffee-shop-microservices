package com.rzk.order_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CreateOrderRequestDto {

    private Integer customerId;
    private Map<Integer, Integer> productQuantities;
    private String note;
}

package com.rzk.order_service.client;

import com.rzk.order_service.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("menu-service")
public interface MenuServiceClient {

    @GetMapping("/products/{id}")
    ProductDto getProduct(@PathVariable Integer id);
}

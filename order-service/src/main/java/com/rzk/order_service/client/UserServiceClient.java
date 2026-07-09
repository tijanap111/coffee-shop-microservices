package com.rzk.order_service.client;

import com.rzk.order_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    UserDto getUser(@PathVariable Integer id);

    @PostMapping("/users/{id}/loyalty/add-points")
    void addPoints(@PathVariable Integer id, @RequestParam Integer points);
}

package com.rzk.order_service.controller;

import com.rzk.order_service.dto.CreateOrderRequestDto;
import com.rzk.order_service.model.Order;
import com.rzk.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Validated
public class OrderController {

    private final OrderService os;

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return new ResponseEntity<>(os.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Integer id) {
        Optional<Order> order = os.getById(id);
        if (order.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order.get(), HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getByCustomer(@PathVariable Integer customerId) {
        return new ResponseEntity<>(os.getByCustomer(customerId), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getByStatus(@PathVariable String status) {
        return new ResponseEntity<>(os.getByStatus(status), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody CreateOrderRequestDto request) {
        Order order = os.createOrder(request.getCustomerId(), request.getProductQuantities(), request.getNote());
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Integer id, @RequestParam String status) {
        return new ResponseEntity<>(os.updateStatus(id, status), HttpStatus.OK);
    }
}

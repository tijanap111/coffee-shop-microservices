package com.rzk.order_service.service;

import com.rzk.order_service.client.MenuServiceClient;
import com.rzk.order_service.client.UserServiceClient;
import com.rzk.order_service.dto.ProductDto;
import com.rzk.order_service.dto.UserDto;
import com.rzk.order_service.model.Customer;
import com.rzk.order_service.model.Order;
import com.rzk.order_service.model.OrderItem;
import com.rzk.order_service.model.ProductSnapshot;
import com.rzk.order_service.repository.CustomerRepository;
import com.rzk.order_service.repository.OrderItemRepository;
import com.rzk.order_service.repository.OrderRepository;
import com.rzk.order_service.repository.ProductSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository or;
    private final OrderItemRepository oir;
    private final CustomerRepository cr;
    private final ProductSnapshotRepository psr;
    private final MenuServiceClient mc;
    private final UserServiceClient uc;

    public List<Order> getAll() {
        return or.findAll();
    }

    public Optional<Order> getById(Integer id) {
        return or.findById(id);
    }

    @Transactional
    public Order createOrder(Integer customerId, Map<Integer, Integer> productQuantities, String note) {
        UserDto user = uc.getUser(customerId);

        if (user == null) {
            throw new RuntimeException("User not found with id: " + customerId);
        }

        Customer customer = cr.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + user.getEmail()));

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus("RECEIVED");
        order.setCreatedAt(Instant.now());
        order.setNote(note);
        order.setTotalPrice(BigDecimal.ZERO);
        Order savedOrder = or.save(order);

        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
            Integer productId = entry.getKey();
            Integer quantity = entry.getValue();

            ProductDto product = mc.getProduct(productId);
            if (product == null || !product.getAvailable()) {
                throw new RuntimeException("Product not available: " + productId);
            }

            ProductSnapshot snapshot = new ProductSnapshot();
            snapshot.setProductId(product.getId());
            snapshot.setName(product.getName());
            snapshot.setPrice(product.getPrice());
            snapshot.setAvailable(product.getAvailable());
            ProductSnapshot savedSnapshot = psr.save(snapshot);

            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setSnapshot(savedSnapshot);
            item.setQuantity(quantity);
            item.setUnitPrice(product.getPrice());
            oir.save(item);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        savedOrder.setTotalPrice(total);
        return or.save(savedOrder);
    }

    @Transactional
    public Order updateStatus(Integer orderId, String newStatus) {
        Order order = or.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        validateStatusTransition(order.getStatus(), newStatus);
        order.setStatus(newStatus);

        if ("PICKED_UP".equals(newStatus)) {
            int pointsEarned = order.getTotalPrice().divide(BigDecimal.TEN).intValue();
            uc.addPoints(order.getCustomer().getId(), pointsEarned);
        }

        return or.save(order);
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        Map<String, List<String>> allowedTransitions = Map.of(
                "RECEIVED", List.of("PREPARING"),
                "PREPARING", List.of("READY"),
                "READY", List.of("PICKED_UP")
        );

        List<String> allowed = allowedTransitions.get(currentStatus);
        if (allowed == null || !allowed.contains(newStatus)) {
            throw new RuntimeException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
    }

    public List<Order> getByCustomer(Integer customerId) {
        return or.findByCustomerId(customerId);
    }

    public List<Order> getByStatus(String status) {
        return or.findByStatus(status);
    }
}

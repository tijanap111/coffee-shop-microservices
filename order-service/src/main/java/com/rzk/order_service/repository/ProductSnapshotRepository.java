package com.rzk.order_service.repository;

import com.rzk.order_service.model.ProductSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSnapshotRepository extends JpaRepository<ProductSnapshot, Integer> {


}

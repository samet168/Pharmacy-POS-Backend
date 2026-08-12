package com.pharmacy.pos.catalog.repository;

import com.pharmacy.pos.catalog.entity.ProductUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductUnitRepository extends JpaRepository<ProductUnit, Long> {
    List<ProductUnit> findByProductId(Long productId);
    boolean existsByBarcode(String barcode);
}

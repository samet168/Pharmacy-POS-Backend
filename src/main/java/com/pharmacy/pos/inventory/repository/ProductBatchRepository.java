package com.pharmacy.pos.inventory.repository;

import com.pharmacy.pos.inventory.entity.ProductBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductBatchRepository extends JpaRepository<ProductBatch, Long> {
    List<ProductBatch> findByProductIdOrderByExpiryDateAsc(Long productId);
    
    @Query("SELECT pb FROM ProductBatch pb WHERE pb.product.id = :productId ORDER BY pb.expiryDate ASC")
    List<ProductBatch> findByProductIdForFefo(@Param("productId") Long productId);
}

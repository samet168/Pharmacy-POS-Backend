package com.pharmacy.pos.inventory.repository;

import com.pharmacy.pos.inventory.entity.ProductBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductBatchRepository extends JpaRepository<ProductBatch, Long> {
    List<ProductBatch> findByProductId(Long productId);
    List<ProductBatch> findByProductIdOrderByExpiryDateAsc(Long productId);
    
    @Query("SELECT pb FROM ProductBatch pb WHERE pb.product.id = :productId ORDER BY pb.expiryDate ASC")
    List<ProductBatch> findByProductIdForFefo(@Param("productId") Long productId);

    @Query("SELECT pb FROM ProductBatch pb WHERE pb.expiryDate BETWEEN :from AND :to")
    List<ProductBatch> findByExpiryDateBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("SELECT pb FROM ProductBatch pb WHERE pb.expiryDate < :date")
    List<ProductBatch> findByExpiryDateBefore(@Param("date") LocalDate date);
}

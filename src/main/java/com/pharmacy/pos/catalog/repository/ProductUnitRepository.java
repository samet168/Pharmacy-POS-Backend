package com.pharmacy.pos.catalog.repository;

import com.pharmacy.pos.catalog.entity.ProductUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductUnitRepository extends JpaRepository<ProductUnit, Long> {

    // product is @ManyToOne — use JPQL traversal
    @Query("SELECT pu FROM ProductUnit pu WHERE pu.product.id = :productId")
    List<ProductUnit> findByProductId(@Param("productId") Long productId);

    @Query("SELECT CASE WHEN COUNT(pu) > 0 THEN true ELSE false END " +
           "FROM ProductUnit pu WHERE pu.barcode = :barcode")
    boolean existsByBarcode(@Param("barcode") String barcode);

    Optional<ProductUnit> findByBarcode(String barcode);
}

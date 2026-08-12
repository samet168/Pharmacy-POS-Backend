package com.pharmacy.pos.catalog.repository;

import com.pharmacy.pos.catalog.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByOrganizationIdAndSku(Long organizationId, String sku);
    Page<Product> findByOrganizationId(Long organizationId, Pageable pageable);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findByDefaultSupplierId(Long supplierId, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.organization.id = :organizationId AND p.active = true")
    Page<Product> findActiveByOrganizationId(@Param("organizationId") Long organizationId, Pageable pageable);
}

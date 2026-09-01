package com.pharmacy.pos.catalog.repository;

import com.pharmacy.pos.catalog.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.organization.id = :orgId AND p.sku = :sku")
    Optional<Product> findByOrganizationIdAndSku(
            @Param("orgId") Long organizationId,
            @Param("sku") String sku);

    /** Paginated — used by ProductService.getByOrganization() */
    @Query("SELECT p FROM Product p WHERE p.organization.id = :orgId")
    Page<Product> findByOrganizationId(
            @Param("orgId") Long organizationId,
            Pageable pageable);

    /** Non-paginated — used by ReportsService.getProductReport() */
    @Query("SELECT p FROM Product p WHERE p.organization.id = :orgId")
    List<Product> findByOrganizationId(@Param("orgId") Long organizationId);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    Page<Product> findByCategoryId(
            @Param("categoryId") Long categoryId,
            Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.defaultSupplier.id = :supplierId")
    Page<Product> findByDefaultSupplierId(
            @Param("supplierId") Long supplierId,
            Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.organization.id = :orgId AND p.active = true")
    Page<Product> findActiveByOrganizationId(
            @Param("orgId") Long organizationId,
            Pageable pageable);

    /**
     * Full-text search by brandName OR sku within an organization.
     * Used by ProductService.search().
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(LOWER(p.brandName) LIKE LOWER(CONCAT('%', :brand, '%')) OR " +
           " LOWER(p.sku)       LIKE LOWER(CONCAT('%', :sku,   '%')))")
    Page<Product> findByBrandNameContainingIgnoreCaseOrSkuContainingIgnoreCase(
            @Param("brand")  String brandName,
            @Param("sku")    String sku,
            Pageable pageable);
}

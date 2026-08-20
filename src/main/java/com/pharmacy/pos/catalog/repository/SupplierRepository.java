package com.pharmacy.pos.catalog.repository;

import com.pharmacy.pos.catalog.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("SELECT s FROM Supplier s WHERE s.organization.id = :orgId")
    List<Supplier> findByOrganizationId(@Param("orgId") Long organizationId);

    @Query("SELECT s FROM Supplier s WHERE s.organization.id = :orgId " +
           "AND LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Supplier> findByOrganizationIdAndNameContainingIgnoreCase(
            @Param("orgId") Long organizationId,
            @Param("name") String name);
}

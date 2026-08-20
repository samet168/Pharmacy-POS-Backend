package com.pharmacy.pos.customer.repository;

import com.pharmacy.pos.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /** Paginated — used by CustomerService.getByOrganization() */
    @Query("SELECT c FROM Customer c WHERE c.organization.id = :orgId")
    Page<Customer> findByOrganizationId(@Param("orgId") Long organizationId, Pageable pageable);

    /** Non-paginated — used by ReportsService.getCustomerReport() */
    @Query("SELECT c FROM Customer c WHERE c.organization.id = :orgId")
    List<Customer> findByOrganizationId(@Param("orgId") Long organizationId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c " +
           "WHERE c.organization.id = :orgId AND c.phone = :phone")
    boolean existsByOrganizationIdAndPhone(
            @Param("orgId") Long organizationId,
            @Param("phone") String phone);

    @Query("SELECT c FROM Customer c WHERE c.organization.id = :orgId AND c.phone = :phone")
    Optional<Customer> findByOrganizationIdAndPhone(
            @Param("orgId") Long organizationId,
            @Param("phone") String phone);

    /**
     * Search by name OR phone within an organization.
     * Used by CustomerService.search().
     */
    @Query("SELECT c FROM Customer c WHERE c.organization.id = :orgId AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           " c.phone        LIKE       CONCAT('%', :phone, '%'))")
    Page<Customer> findByOrganizationIdAndNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(
            @Param("orgId")  Long organizationId,
            @Param("name")   String name,
            @Param("phone")  String phone,
            Pageable pageable);
}

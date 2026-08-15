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
    Page<Customer> findByOrganizationId(Long organizationId, Pageable pageable);
    boolean existsByOrganizationIdAndPhone(Long organizationId, String phone);
    Optional<Customer> findByOrganizationIdAndPhone(Long organizationId, String phone);
    List<Customer> findByOrganizationId(Long organizationId);
    
    Page<Customer> findByOrganizationIdAndNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(
            Long organizationId, String name, String phone, Pageable pageable);
}

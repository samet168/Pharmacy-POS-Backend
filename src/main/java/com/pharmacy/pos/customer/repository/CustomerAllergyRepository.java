package com.pharmacy.pos.customer.repository;

import com.pharmacy.pos.customer.entity.CustomerAllergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAllergyRepository extends JpaRepository<CustomerAllergy, Long> {
    List<CustomerAllergy> findByCustomerId(Long customerId);
}

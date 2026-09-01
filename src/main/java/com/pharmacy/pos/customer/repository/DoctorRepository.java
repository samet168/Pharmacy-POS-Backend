package com.pharmacy.pos.customer.repository;

import com.pharmacy.pos.customer.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Page<Doctor> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Doctor> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}

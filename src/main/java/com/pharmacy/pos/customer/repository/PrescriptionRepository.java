package com.pharmacy.pos.customer.repository;

import com.pharmacy.pos.customer.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Page<Prescription> findByCustomerId(Long customerId, Pageable pageable);
    Page<Prescription> findByDoctorId(Long doctorId, Pageable pageable);
    
    @Transactional
    void deleteByDoctorId(Long doctorId);
}

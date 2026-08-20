package com.pharmacy.pos.customer.repository;

import com.pharmacy.pos.customer.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    // customer and doctor are @ManyToOne
    @Query("SELECT p FROM Prescription p WHERE p.customer.id = :customerId")
    Page<Prescription> findByCustomerId(@Param("customerId") Long customerId, Pageable pageable);

    @Query("SELECT p FROM Prescription p WHERE p.doctor.id = :doctorId")
    Page<Prescription> findByDoctorId(@Param("doctorId") Long doctorId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Prescription p WHERE p.doctor.id = :doctorId")
    void deleteByDoctorId(@Param("doctorId") Long doctorId);
}

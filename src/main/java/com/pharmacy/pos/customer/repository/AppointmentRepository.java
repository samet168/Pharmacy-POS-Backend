package com.pharmacy.pos.customer.repository;

import com.pharmacy.pos.customer.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByAppointmentNumber(String appointmentNumber);
    List<Appointment> findByPatientPhoneOrderByCreatedAtDesc(String patientPhone);
    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);
}

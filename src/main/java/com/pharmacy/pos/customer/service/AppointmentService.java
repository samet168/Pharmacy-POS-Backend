package com.pharmacy.pos.customer.service;

import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.customer.dto.AppointmentRequest;
import com.pharmacy.pos.customer.dto.AppointmentResponse;
import com.pharmacy.pos.customer.entity.Appointment;
import com.pharmacy.pos.customer.entity.Doctor;
import com.pharmacy.pos.customer.repository.AppointmentRepository;
import com.pharmacy.pos.customer.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", request.getDoctorId()));

        String aptNum = "APT-" + String.format("%05d", System.currentTimeMillis() % 100000);

        Appointment appointment = new Appointment();
        appointment.setAppointmentNumber(aptNum);
        appointment.setDoctor(doctor);
        appointment.setDoctorName(doctor.getName());
        appointment.setDoctorSpecialty(doctor.getSpecialty() != null ? doctor.getSpecialty() : "General Medicine");
        appointment.setPatientName(request.getPatientName());
        appointment.setPatientPhone(request.getPatientPhone());
        appointment.setPatientEmail(request.getPatientEmail());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setType(request.getType() != null ? request.getType() : "IN_PERSON");
        appointment.setStatus("PENDING");
        appointment.setSymptoms(request.getSymptoms());
        String branch = doctor.getClinicName() != null && !doctor.getClinicName().isBlank()
                ? doctor.getClinicName()
                : (request.getBranchName() != null ? request.getBranchName() : "សាខាកណ្តាល (Main Branch)");
        appointment.setClinicName(branch);
        appointment.setFee(request.getFee());
        appointment.setQrCode(aptNum + "-QR-" + System.currentTimeMillis() % 10000);

        appointment = appointmentRepository.save(appointment);
        return toResponse(appointment);
    }

    public AppointmentResponse getById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));
        return toResponse(appointment);
    }

    public AppointmentResponse getByNumber(String number) {
        Appointment appointment = appointmentRepository.findByAppointmentNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment number: " + number));
        return toResponse(appointment);
    }

    public List<AppointmentResponse> getByPatientPhone(String phone) {
        return appointmentRepository.findByPatientPhoneOrderByCreatedAtDesc(phone).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Page<AppointmentResponse> getAll(Pageable pageable) {
        return appointmentRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional
    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));
        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
    }

    @Transactional
    public AppointmentResponse updateStatus(Long id, String status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));
        appointment.setStatus(status);
        appointment = appointmentRepository.save(appointment);
        return toResponse(appointment);
    }

    private AppointmentResponse toResponse(Appointment entity) {
        AppointmentResponse dto = new AppointmentResponse();
        dto.setId(entity.getId());
        dto.setAppointmentNumber(entity.getAppointmentNumber());
        if (entity.getDoctor() != null) {
            dto.setDoctorId(entity.getDoctor().getId());
            dto.setDoctorImage(entity.getDoctor().getImageUrl());
        }
        dto.setDoctorName(entity.getDoctorName());
        dto.setDoctorSpecialty(entity.getDoctorSpecialty());
        dto.setPatientName(entity.getPatientName());
        dto.setPatientPhone(entity.getPatientPhone());
        dto.setPatientEmail(entity.getPatientEmail());
        dto.setAppointmentDate(entity.getAppointmentDate());
        dto.setAppointmentTime(entity.getAppointmentTime());
        dto.setType(entity.getType());
        dto.setStatus(entity.getStatus());
        dto.setSymptoms(entity.getSymptoms());
        dto.setClinicName(entity.getClinicName());
        dto.setBranchName(entity.getClinicName());
        dto.setFee(entity.getFee());
        dto.setQrCode(entity.getQrCode());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}

package com.pharmacy.pos.customer.service;

import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.customer.dto.DoctorRequest;
import com.pharmacy.pos.customer.dto.DoctorResponse;
import com.pharmacy.pos.customer.entity.Doctor;
import com.pharmacy.pos.customer.mapper.DoctorMapper;
import com.pharmacy.pos.customer.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Transactional
    public DoctorResponse create(DoctorRequest request) {
        Doctor doctor = doctorMapper.toEntity(request);
        doctor = doctorRepository.save(doctor);
        return doctorMapper.toResponse(doctor);
    }

    @Transactional
    public DoctorResponse update(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));

        doctorMapper.updateEntityFromRequest(request, doctor);
        doctor = doctorRepository.save(doctor);
        return doctorMapper.toResponse(doctor);
    }

    public DoctorResponse getById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));
        return doctorMapper.toResponse(doctor);
    }

    public Page<DoctorResponse> searchByName(String name, Pageable pageable) {
        return doctorRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(doctorMapper::toResponse);
    }

    public Page<DoctorResponse> getAll(Pageable pageable) {
        return doctorRepository.findAll(pageable)
                .map(doctorMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));
        doctorRepository.delete(doctor);
    }
}

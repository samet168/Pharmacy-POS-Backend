package com.pharmacy.pos.customer.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.customer.dto.DoctorRequest;
import com.pharmacy.pos.customer.dto.DoctorResponse;
import com.pharmacy.pos.customer.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasAuthority('doctor.create')")
    public ApiResponse<DoctorResponse> create(@Valid @RequestBody DoctorRequest request) {
        return ApiResponse.success(doctorService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('doctor.update')")
    public ApiResponse<DoctorResponse> update(@PathVariable Long id, @Valid @RequestBody DoctorRequest request) {
        return ApiResponse.success(doctorService.update(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('doctor.view')")
    public ApiResponse<DoctorResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(doctorService.getById(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('doctor.view')")
    public ApiResponse<PageResponse<DoctorResponse>> searchByName(@RequestParam String name, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(doctorService.searchByName(name, pageable)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('doctor.view')")
    public ApiResponse<PageResponse<DoctorResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(doctorService.getAll(pageable)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('doctor.delete')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ApiResponse.success("Doctor deleted successfully", null);
    }
}

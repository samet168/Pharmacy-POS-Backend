package com.pharmacy.pos.customer.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.customer.dto.PrescriptionRequest;
import com.pharmacy.pos.customer.dto.PrescriptionResponse;
import com.pharmacy.pos.customer.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    public ApiResponse<PrescriptionResponse> create(@Valid @RequestBody PrescriptionRequest request) {
        return ApiResponse.success(prescriptionService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<PrescriptionResponse> update(@PathVariable Long id, @Valid @RequestBody PrescriptionRequest request) {
        return ApiResponse.success(prescriptionService.update(id, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<PrescriptionResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(prescriptionService.getById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<PageResponse<PrescriptionResponse>> getByCustomer(@PathVariable Long customerId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(prescriptionService.getByCustomer(customerId, pageable)));
    }

    @GetMapping("/doctor/{doctorId}")
    public ApiResponse<PageResponse<PrescriptionResponse>> getByDoctor(@PathVariable Long doctorId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(prescriptionService.getByDoctor(doctorId, pageable)));
    }

    @GetMapping
    public ApiResponse<PageResponse<PrescriptionResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(prescriptionService.getAll(pageable)));
    }

    @PostMapping("/check-allergies")
    public ApiResponse<List<PrescriptionService.AllergyMatch>> checkProductAllergies(
            @RequestParam Long customerId,
            @RequestBody List<Long> productIds) {
        return ApiResponse.success(prescriptionService.checkProductAllergies(customerId, productIds));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        prescriptionService.delete(id);
        return ApiResponse.success("Prescription deleted successfully", null);
    }
}

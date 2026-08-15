package com.pharmacy.pos.customer.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.customer.dto.DoctorRequest;
import com.pharmacy.pos.customer.dto.DoctorResponse;
import com.pharmacy.pos.customer.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('doctor.create')")
    @Operation(summary = "Create doctor", description = "Create a new doctor with optional image upload")
    public ApiResponse<DoctorResponse> create(
            @Parameter(description = "Doctor data as JSON", required = true, content = @Content(schema = @Schema(implementation = DoctorRequest.class))) @RequestPart(value = "doctor", required = true) @Valid DoctorRequest request,
            @Parameter(description = "Image file (optional)") @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            return ApiResponse.success(doctorService.createWithImage(request, file));
        }
        return ApiResponse.success(doctorService.create(request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('doctor.update')")
    @Operation(summary = "Update doctor", description = "Update doctor with optional image upload")
    public ApiResponse<DoctorResponse> update(
            @PathVariable Long id,
            @Parameter(description = "Doctor data as JSON", required = true, content = @Content(schema = @Schema(implementation = DoctorRequest.class))) @RequestPart(value = "doctor", required = true) @Valid DoctorRequest request,
            @Parameter(description = "Image file (optional)") @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            return ApiResponse.success(doctorService.updateWithImage(id, request, file));
        }
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

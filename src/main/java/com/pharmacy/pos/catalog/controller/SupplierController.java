package com.pharmacy.pos.catalog.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.catalog.dto.SupplierRequest;
import com.pharmacy.pos.catalog.dto.SupplierResponse;
import com.pharmacy.pos.catalog.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public ApiResponse<List<SupplierResponse>> getAll(
            @RequestParam(required = false) Long organizationId,
            org.springframework.security.core.Authentication authentication) {
        Long orgId = organizationId;
        if (orgId == null && authentication != null && authentication.getPrincipal() instanceof com.pharmacy.pos.security.CustomUserDetails userDetails) {
            if (userDetails.getUser().getOrganization() != null) {
                orgId = userDetails.getUser().getOrganization().getId();
            }
        }
        if (orgId != null) {
            return ApiResponse.success(supplierService.getByOrganization(orgId));
        }
        return ApiResponse.success(supplierService.getAll());
    }

    @GetMapping("/organization/{organizationId}")
    public ApiResponse<List<SupplierResponse>> getByOrganization(@PathVariable Long organizationId) {
        return ApiResponse.success(supplierService.getByOrganization(organizationId));
    }

    @GetMapping("/search")
    public ApiResponse<List<SupplierResponse>> search(@RequestParam Long organizationId, @RequestParam String q) {
        return ApiResponse.success(supplierService.search(organizationId, q));
    }

    @GetMapping("/{id}")
    public ApiResponse<SupplierResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(supplierService.getById(id));
    }

    @PostMapping
    public ApiResponse<SupplierResponse> create(@Valid @RequestBody SupplierRequest request) {
        return ApiResponse.success(supplierService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<SupplierResponse> update(@PathVariable Long id, @Valid @RequestBody SupplierRequest request) {
        return ApiResponse.success(supplierService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return ApiResponse.success("Supplier deleted successfully", null);
    }
}
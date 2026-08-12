package com.pharmacy.pos.tenant.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.tenant.dto.OrganizationRequest;
import com.pharmacy.pos.tenant.dto.OrganizationResponse;
import com.pharmacy.pos.tenant.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    @PreAuthorize("hasAuthority('organization.create')")
    public ApiResponse<OrganizationResponse> create(@Valid @RequestBody OrganizationRequest request) {
        return ApiResponse.success(organizationService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('organization.update')")
    public ApiResponse<OrganizationResponse> update(@PathVariable Long id, @Valid @RequestBody OrganizationRequest request) {
        return ApiResponse.success(organizationService.update(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('organization.view')")
    public ApiResponse<OrganizationResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(organizationService.getById(id));
    }

    @GetMapping("/slug/{slug}")
    @PreAuthorize("hasAuthority('organization.view')")
    public ApiResponse<OrganizationResponse> getBySlug(@PathVariable String slug) {
        return ApiResponse.success(organizationService.getBySlug(slug));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('organization.view')")
    public ApiResponse<PageResponse<OrganizationResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(organizationService.getAll(pageable)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('organization.delete')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        organizationService.delete(id);
        return ApiResponse.success("Organization deleted successfully", null);
    }
}

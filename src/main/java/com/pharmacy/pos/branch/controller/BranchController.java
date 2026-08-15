package com.pharmacy.pos.branch.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.branch.dto.BranchRequest;
import com.pharmacy.pos.branch.dto.BranchResponse;
import com.pharmacy.pos.branch.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    @PreAuthorize("hasAuthority('branch.create')")
    public ApiResponse<BranchResponse> create(@Valid @RequestBody BranchRequest request) {
        return ApiResponse.success(branchService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('branch.update')")
    public ApiResponse<BranchResponse> update(@PathVariable Long id, @Valid @RequestBody BranchRequest request) {
        return ApiResponse.success(branchService.update(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('branch.view')")
    public ApiResponse<BranchResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(branchService.getById(id));
    }

    @GetMapping("/organization/{organizationId}")
    @PreAuthorize("hasAuthority('branch.view')")
    public ApiResponse<PageResponse<BranchResponse>> getByOrganization(@PathVariable Long organizationId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(branchService.getByOrganization(organizationId, pageable)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('branch.view')")
    public ApiResponse<PageResponse<BranchResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(branchService.getAll(pageable)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('branch.delete')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        branchService.delete(id);
        return ApiResponse.success("Branch deleted successfully", null);
    }
}

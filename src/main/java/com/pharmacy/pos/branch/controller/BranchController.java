package com.pharmacy.pos.branch.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.branch.dto.BranchRequest;
import com.pharmacy.pos.branch.dto.BranchResponse;
import com.pharmacy.pos.branch.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public ApiResponse<BranchResponse> create(@Valid @RequestBody BranchRequest request) {
        return ApiResponse.success(branchService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<BranchResponse> update(@PathVariable Long id, @Valid @RequestBody BranchRequest request) {
        return ApiResponse.success(branchService.update(id, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<BranchResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(branchService.getById(id));
    }

    @GetMapping("/organization/{organizationId}")
    public ApiResponse<PageResponse<BranchResponse>> getByOrganization(@PathVariable Long organizationId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(branchService.getByOrganization(organizationId, pageable)));
    }

    @GetMapping
    public ApiResponse<PageResponse<BranchResponse>> getAll(
            @RequestParam(required = false) Long organizationId,
            org.springframework.security.core.Authentication authentication,
            Pageable pageable) {
        Long orgId = organizationId;
        if (orgId == null && authentication != null && authentication.getPrincipal() instanceof com.pharmacy.pos.security.CustomUserDetails userDetails) {
            if (userDetails.getUser().getOrganization() != null) {
                orgId = userDetails.getUser().getOrganization().getId();
            }
        }
        if (orgId != null) {
            return ApiResponse.success(PageResponse.of(branchService.getByOrganization(orgId, pageable)));
        }
        return ApiResponse.success(PageResponse.of(branchService.getAll(pageable)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        branchService.delete(id);
        return ApiResponse.success("Branch deleted successfully", null);
    }
}

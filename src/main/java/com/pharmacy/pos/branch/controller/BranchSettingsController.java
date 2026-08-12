package com.pharmacy.pos.branch.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.branch.dto.BranchSettingsRequest;
import com.pharmacy.pos.branch.dto.BranchSettingsResponse;
import com.pharmacy.pos.branch.service.BranchSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/branch-settings")
@RequiredArgsConstructor
public class BranchSettingsController {

    private final BranchSettingsService branchSettingsService;

    @PostMapping
    @PreAuthorize("hasAuthority('branch.settings.update')")
    public ApiResponse<BranchSettingsResponse> createOrUpdate(@Valid @RequestBody BranchSettingsRequest request) {
        return ApiResponse.success(branchSettingsService.createOrUpdate(request));
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasAuthority('branch.settings.view')")
    public ApiResponse<BranchSettingsResponse> getByBranchId(@PathVariable Long branchId) {
        return ApiResponse.success(branchSettingsService.getByBranchId(branchId));
    }
}

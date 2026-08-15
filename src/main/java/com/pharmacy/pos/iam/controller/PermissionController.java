package com.pharmacy.pos.iam.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.iam.dto.PermissionRequest;
import com.pharmacy.pos.iam.dto.PermissionResponse;
import com.pharmacy.pos.iam.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> create(@Valid @RequestBody PermissionRequest request) {
        return ApiResponse.success(permissionService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<PermissionResponse> update(@PathVariable Long id, @Valid @RequestBody PermissionRequest request) {
        return ApiResponse.success(permissionService.update(id, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<PermissionResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(permissionService.getById(id));
    }

    @GetMapping("/code/{code}")
    public ApiResponse<PermissionResponse> getByCode(@PathVariable String code) {
        return ApiResponse.success(permissionService.getByCode(code));
    }

    @GetMapping
    public ApiResponse<PageResponse<PermissionResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(permissionService.getAll(pageable)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return ApiResponse.success("Permission deleted successfully", null);
    }
}

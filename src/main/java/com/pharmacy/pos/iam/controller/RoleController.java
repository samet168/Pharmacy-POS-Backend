package com.pharmacy.pos.iam.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.iam.dto.RoleRequest;
import com.pharmacy.pos.iam.dto.RoleResponse;
import com.pharmacy.pos.iam.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @PreAuthorize("hasAuthority('role.create')")
    public ApiResponse<RoleResponse> create(@Valid @RequestBody RoleRequest request) {
        return ApiResponse.success(roleService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role.update')")
    public ApiResponse<RoleResponse> update(@PathVariable Long id, @Valid @RequestBody RoleRequest request) {
        return ApiResponse.success(roleService.update(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role.view')")
    public ApiResponse<RoleResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(roleService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('role.view')")
    public ApiResponse<PageResponse<RoleResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(roleService.getAll(pageable)));
    }

    @GetMapping("/organization/{organizationId}")
    @PreAuthorize("hasAuthority('role.view')")
    public ApiResponse<PageResponse<RoleResponse>> getByOrganization(
            @PathVariable Long organizationId,
            Pageable pageable) {
        return ApiResponse.success(PageResponse.of(roleService.getByOrganization(organizationId, pageable)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role.delete')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ApiResponse.success("Role deleted successfully", null);
    }
}

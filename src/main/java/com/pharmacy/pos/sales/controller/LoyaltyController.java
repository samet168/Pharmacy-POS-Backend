package com.pharmacy.pos.sales.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.sales.dto.LoyaltyRequest;
import com.pharmacy.pos.sales.dto.LoyaltyResponse;
import com.pharmacy.pos.sales.service.LoyaltyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loyalty")
@RequiredArgsConstructor
public class LoyaltyController {

    private final LoyaltyService loyaltyService;

    @PostMapping
    @Operation(summary = "Create loyalty program", description = "Create a new loyalty program")
    public ApiResponse<LoyaltyResponse> create(@Valid @RequestBody LoyaltyRequest request) {
        return ApiResponse.success(loyaltyService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update loyalty program", description = "Update an existing loyalty program")
    public ApiResponse<LoyaltyResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody LoyaltyRequest request) {
        return ApiResponse.success(loyaltyService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get loyalty program by ID", description = "Retrieve a loyalty program by its ID")
    public ApiResponse<LoyaltyResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(loyaltyService.getById(id));
    }

    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Get loyalty programs by organization", description = "Retrieve all loyalty programs for a specific organization")
    public ApiResponse<List<LoyaltyResponse>> getByOrganization(@PathVariable Long organizationId) {
        return ApiResponse.success(loyaltyService.getByOrganization(organizationId));
    }

    @GetMapping("/organization/{organizationId}/active")
    @Operation(summary = "Get active loyalty program by organization", description = "Retrieve the active loyalty program for a specific organization")
    public ApiResponse<LoyaltyResponse> getActiveByOrganization(@PathVariable Long organizationId) {
        return ApiResponse.success(loyaltyService.getActiveByOrganization(organizationId));
    }

    @GetMapping
    @Operation(summary = "Get all loyalty programs", description = "Retrieve all loyalty programs")
    public ApiResponse<List<LoyaltyResponse>> getAll() {
        return ApiResponse.success(loyaltyService.getAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete loyalty program", description = "Delete a loyalty program by its ID")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        loyaltyService.delete(id);
        return ApiResponse.success("Loyalty program deleted successfully", null);
    }
}

package com.pharmacy.pos.sales.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.sales.dto.PromotionRequest;
import com.pharmacy.pos.sales.dto.PromotionResponse;
import com.pharmacy.pos.sales.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionsController {

    private final PromotionService promotionService;

    @PostMapping
    @Operation(summary = "Create promotion", description = "Create a new promotion")
    public ApiResponse<PromotionResponse> create(@Valid @RequestBody PromotionRequest request) {
        return ApiResponse.success(promotionService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update promotion", description = "Update an existing promotion")
    public ApiResponse<PromotionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PromotionRequest request) {
        return ApiResponse.success(promotionService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get promotion by ID", description = "Retrieve a promotion by its ID")
    public ApiResponse<PromotionResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(promotionService.getById(id));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get promotion by code", description = "Retrieve a promotion by its code")
    public ApiResponse<PromotionResponse> getByCode(@PathVariable String code) {
        return ApiResponse.success(promotionService.getByCode(code));
    }

    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Get promotions by organization", description = "Retrieve all promotions for a specific organization")
    public ApiResponse<List<PromotionResponse>> getByOrganization(@PathVariable Long organizationId) {
        return ApiResponse.success(promotionService.getByOrganization(organizationId));
    }

    @GetMapping("/organization/{organizationId}/active")
    @Operation(summary = "Get active promotions by organization", description = "Retrieve all active promotions for a specific organization")
    public ApiResponse<List<PromotionResponse>> getActiveByOrganization(@PathVariable Long organizationId) {
        return ApiResponse.success(promotionService.getActiveByOrganization(organizationId));
    }

    @GetMapping("/organization/{organizationId}/active/{date}")
    @Operation(summary = "Get active promotions by organization and date", description = "Retrieve all active promotions for a specific organization on a specific date")
    public ApiResponse<List<PromotionResponse>> getActiveByOrganizationAndDate(
            @PathVariable Long organizationId,
            @PathVariable LocalDate date) {
        return ApiResponse.success(promotionService.getActiveByOrganizationAndDate(organizationId, date));
    }

    @GetMapping
    @Operation(summary = "Get all promotions", description = "Retrieve all promotions")
    public ApiResponse<List<PromotionResponse>> getAll() {
        return ApiResponse.success(promotionService.getAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete promotion", description = "Delete a promotion by its ID")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        promotionService.delete(id);
        return ApiResponse.success("Promotion deleted successfully", null);
    }
}

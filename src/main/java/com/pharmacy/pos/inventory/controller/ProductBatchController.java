package com.pharmacy.pos.inventory.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.inventory.dto.ProductBatchRequest;
import com.pharmacy.pos.inventory.dto.ProductBatchResponse;
import com.pharmacy.pos.inventory.service.ProductBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-batches")
@RequiredArgsConstructor
@Tag(name = "Product Batches", description = "Product batch management API")
public class ProductBatchController {

    private final ProductBatchService productBatchService;

    @GetMapping
    @Operation(summary = "Get all product batches")
    public ApiResponse<List<ProductBatchResponse>> getAllBatches() {
        return ApiResponse.success(productBatchService.getAllBatches());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product batch by ID")
    public ApiResponse<ProductBatchResponse> getBatchById(@PathVariable Long id) {
        return ApiResponse.success(productBatchService.getBatchById(id));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get batches by product ID")
    public ApiResponse<List<ProductBatchResponse>> getBatchesByProductId(@PathVariable Long productId) {
        return ApiResponse.success(productBatchService.getBatchesByProductId(productId));
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get batches by branch ID")
    public ApiResponse<List<ProductBatchResponse>> getBatchesByBranchId(@PathVariable Long branchId) {
        return ApiResponse.success(productBatchService.getBatchesByBranchId(branchId));
    }

    @GetMapping("/branch/{branchId}/expiring")
    @Operation(summary = "Get expiring batches by branch ID")
    public ApiResponse<List<ProductBatchResponse>> getExpiringBatchesByBranch(
            @PathVariable Long branchId,
            @RequestParam(required = false, defaultValue = "30") int withinDays) {
        return ApiResponse.success(productBatchService.getExpiringBatches(branchId, withinDays));
    }

    @GetMapping("/expiring")
    @Operation(summary = "Get all expiring batches")
    public ApiResponse<List<ProductBatchResponse>> getAllExpiringBatches(
            @RequestParam(required = false, defaultValue = "30") int withinDays) {
        return ApiResponse.success(productBatchService.getExpiringBatches(null, withinDays));
    }

    @GetMapping("/branch/{branchId}/expired")
    @Operation(summary = "Get expired batches by branch ID")
    public ApiResponse<List<ProductBatchResponse>> getExpiredBatchesByBranch(@PathVariable Long branchId) {
        return ApiResponse.success(productBatchService.getExpiredBatches(branchId));
    }

    @GetMapping("/expired")
    @Operation(summary = "Get all expired batches")
    public ApiResponse<List<ProductBatchResponse>> getAllExpiredBatches() {
        return ApiResponse.success(productBatchService.getExpiredBatches(null));
    }

    @PostMapping
    @Operation(summary = "Create new product batch")
    public ApiResponse<ProductBatchResponse> createBatch(@Valid @RequestBody ProductBatchRequest request) {
        return ApiResponse.success(productBatchService.createBatch(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product batch")
    public ApiResponse<ProductBatchResponse> updateBatch(@PathVariable Long id, @Valid @RequestBody ProductBatchRequest request) {
        return ApiResponse.success(productBatchService.updateBatch(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product batch")
    public ApiResponse<Void> deleteBatch(@PathVariable Long id) {
        productBatchService.deleteBatch(id);
        return ApiResponse.success(null);
    }
}
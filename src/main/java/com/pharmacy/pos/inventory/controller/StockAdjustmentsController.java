package com.pharmacy.pos.inventory.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.inventory.dto.StockAdjustmentRequest;
import com.pharmacy.pos.inventory.dto.StockAdjustmentResponse;
import com.pharmacy.pos.inventory.service.StockAdjustmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock-adjustments")
@RequiredArgsConstructor
public class StockAdjustmentsController {

    private final StockAdjustmentService stockAdjustmentService;

    @PostMapping
    @Operation(summary = "Create stock adjustment", description = "Create a new stock adjustment record")
    public ApiResponse<StockAdjustmentResponse> create(@Valid @RequestBody StockAdjustmentRequest request) {
        return ApiResponse.success(stockAdjustmentService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update stock adjustment", description = "Update an existing stock adjustment record")
    public ApiResponse<StockAdjustmentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StockAdjustmentRequest request) {
        return ApiResponse.success(stockAdjustmentService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get stock adjustment by ID", description = "Retrieve a stock adjustment record by its ID")
    public ApiResponse<StockAdjustmentResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(stockAdjustmentService.getById(id));
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get stock adjustments by branch", description = "Retrieve all stock adjustments for a specific branch")
    public ApiResponse<List<StockAdjustmentResponse>> getByBranch(@PathVariable Long branchId) {
        return ApiResponse.success(stockAdjustmentService.getByBranch(branchId));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get stock adjustments by product", description = "Retrieve all stock adjustments for a specific product")
    public ApiResponse<List<StockAdjustmentResponse>> getByProduct(@PathVariable Long productId) {
        return ApiResponse.success(stockAdjustmentService.getByProduct(productId));
    }

    @GetMapping("/reason/{reason}")
    @Operation(summary = "Get stock adjustments by reason", description = "Retrieve all stock adjustments for a specific reason")
    public ApiResponse<List<StockAdjustmentResponse>> getByReason(@PathVariable String reason) {
        return ApiResponse.success(stockAdjustmentService.getByReason(reason));
    }

    @GetMapping
    @Operation(summary = "Get all stock adjustments", description = "Retrieve all stock adjustment records")
    public ApiResponse<List<StockAdjustmentResponse>> getAll() {
        return ApiResponse.success(stockAdjustmentService.getAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete stock adjustment", description = "Delete a stock adjustment record by its ID")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        stockAdjustmentService.delete(id);
        return ApiResponse.success("Stock adjustment deleted successfully", null);
    }
}

package com.pharmacy.pos.inventory.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.inventory.dto.BranchInventoryRequest;
import com.pharmacy.pos.inventory.dto.BranchInventoryResponse;
import com.pharmacy.pos.inventory.dto.ExpiringProductResponse;
import com.pharmacy.pos.inventory.service.BranchInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class BranchInventoryController {

    private final BranchInventoryService branchInventoryService;

    @PostMapping("/branch-inventory")
    @Operation(summary = "Create branch inventory", description = "Create a new branch inventory record")
    public ApiResponse<BranchInventoryResponse> create(@Valid @RequestBody BranchInventoryRequest request) {
        return ApiResponse.success(branchInventoryService.create(request));
    }

    @PutMapping("/branch-inventory/{id}")
    @Operation(summary = "Update branch inventory", description = "Update an existing branch inventory record")
    public ApiResponse<BranchInventoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BranchInventoryRequest request) {
        return ApiResponse.success(branchInventoryService.update(id, request));
    }

    @GetMapping("/branch-inventory/{id}")
    @Operation(summary = "Get branch inventory by ID", description = "Retrieve a branch inventory record by its ID")
    public ApiResponse<BranchInventoryResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(branchInventoryService.getById(id));
    }

    @GetMapping("/branch-inventory/branch/{branchId}")
    @Operation(summary = "Get branch inventory by branch", description = "Retrieve all inventory records for a specific branch")
    public ApiResponse<List<BranchInventoryResponse>> getByBranch(@PathVariable Long branchId) {
        return ApiResponse.success(branchInventoryService.getByBranch(branchId));
    }

    @GetMapping("/branch-inventory/branch/{branchId}/product/{productId}/available")
    @Operation(summary = "Get available batches by branch and product", description = "Retrieve available batches for a specific branch and product")
    public ApiResponse<List<BranchInventoryResponse>> getAvailableBatches(
            @PathVariable Long branchId,
            @PathVariable Long productId) {
        return ApiResponse.success(branchInventoryService.getAvailableBatchesByBranchAndProduct(branchId, productId));
    }

    @GetMapping("/branch-inventory")
    @Operation(summary = "Get all branch inventory", description = "Retrieve all branch inventory records")
    public ApiResponse<List<BranchInventoryResponse>> getAll() {
        return ApiResponse.success(branchInventoryService.getAll());
    }

    @DeleteMapping("/branch-inventory/{id}")
    @Operation(summary = "Delete branch inventory", description = "Delete a branch inventory record by its ID")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        branchInventoryService.delete(id);
        return ApiResponse.success("Branch inventory deleted successfully", null);
    }

    @GetMapping("/expiring")
    @Operation(summary = "Get expiring products", description = "Get products approaching expiration")
    public ApiResponse<List<ExpiringProductResponse>> getExpiringProducts(
            @Parameter(description = "Branch ID (optional)") @RequestParam(required = false) Long branchId,
            @Parameter(description = "Days threshold") @RequestParam(defaultValue = "30") int days) {
        return ApiResponse.success(branchInventoryService.getExpiringProducts(branchId, days));
    }

    @GetMapping("/expired")
    @Operation(summary = "Get expired products", description = "Get products that have expired")
    public ApiResponse<List<ExpiringProductResponse>> getExpiredProducts(
            @Parameter(description = "Branch ID (optional)") @RequestParam(required = false) Long branchId) {
        return ApiResponse.success(branchInventoryService.getExpiredProducts(branchId));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock products", description = "Get products with low stock levels")
    public ApiResponse<List<BranchInventoryResponse>> getLowStock(
            @Parameter(description = "Branch ID (optional)") @RequestParam(required = false) Long branchId) {
        return ApiResponse.success(branchInventoryService.getLowStock(branchId));
    }
}

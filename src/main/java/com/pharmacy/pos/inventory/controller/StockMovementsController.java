package com.pharmacy.pos.inventory.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.inventory.dto.StockMovementRequest;
import com.pharmacy.pos.inventory.dto.StockMovementResponse;
import com.pharmacy.pos.inventory.service.StockMovementService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock-movements")
@RequiredArgsConstructor
public class StockMovementsController {

    private final StockMovementService stockMovementService;

    @PostMapping
    @Operation(summary = "Create stock movement", description = "Create a new stock movement record")
    public ApiResponse<StockMovementResponse> create(@Valid @RequestBody StockMovementRequest request) {
        return ApiResponse.success(stockMovementService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update stock movement", description = "Update an existing stock movement record")
    public ApiResponse<StockMovementResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StockMovementRequest request) {
        return ApiResponse.success(stockMovementService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get stock movement by ID", description = "Retrieve a stock movement record by its ID")
    public ApiResponse<StockMovementResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(stockMovementService.getById(id));
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get stock movements by branch", description = "Retrieve all stock movements for a specific branch")
    public ApiResponse<List<StockMovementResponse>> getByBranch(@PathVariable Long branchId) {
        return ApiResponse.success(stockMovementService.getByBranch(branchId));
    }

    @GetMapping("/batch/{batchId}")
    @Operation(summary = "Get stock movements by batch", description = "Retrieve all stock movements for a specific batch")
    public ApiResponse<List<StockMovementResponse>> getByBatch(@PathVariable Long batchId) {
        return ApiResponse.success(stockMovementService.getByBatch(batchId));
    }

    @GetMapping("/reference")
    @Operation(summary = "Get stock movements by reference", description = "Retrieve all stock movements for a specific reference")
    public ApiResponse<List<StockMovementResponse>> getByReference(
            @RequestParam String referenceTable,
            @RequestParam Long referenceId) {
        return ApiResponse.success(stockMovementService.getByReference(referenceTable, referenceId));
    }

    @GetMapping
    @Operation(summary = "Get all stock movements", description = "Retrieve all stock movement records")
    public ApiResponse<List<StockMovementResponse>> getAll() {
        return ApiResponse.success(stockMovementService.getAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete stock movement", description = "Delete a stock movement record by its ID")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        stockMovementService.delete(id);
        return ApiResponse.success("Stock movement deleted successfully", null);
    }
}

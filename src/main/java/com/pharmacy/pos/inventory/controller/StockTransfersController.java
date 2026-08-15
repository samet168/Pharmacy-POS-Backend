package com.pharmacy.pos.inventory.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.enums.TransferStatus;
import com.pharmacy.pos.inventory.dto.StockTransferRequest;
import com.pharmacy.pos.inventory.dto.StockTransferResponse;
import com.pharmacy.pos.inventory.service.StockTransferService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock-transfers")
@RequiredArgsConstructor
public class StockTransfersController {

    private final StockTransferService stockTransferService;

    @PostMapping
    @Operation(summary = "Create stock transfer", description = "Create a new stock transfer request")
    public ApiResponse<StockTransferResponse> create(@Valid @RequestBody StockTransferRequest request) {
        return ApiResponse.success(stockTransferService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update stock transfer", description = "Update an existing stock transfer")
    public ApiResponse<StockTransferResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StockTransferRequest request) {
        return ApiResponse.success(stockTransferService.update(id, request));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve stock transfer", description = "Approve a pending stock transfer")
    public ApiResponse<StockTransferResponse> approve(
            @PathVariable Long id,
            @RequestParam Long approvedBy) {
        return ApiResponse.success(stockTransferService.approve(id, approvedBy));
    }

    @PutMapping("/{id}/receive")
    @Operation(summary = "Receive stock transfer", description = "Mark a stock transfer as received")
    public ApiResponse<StockTransferResponse> receive(
            @PathVariable Long id,
            @RequestParam Long receivedBy) {
        return ApiResponse.success(stockTransferService.receive(id, receivedBy));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get stock transfer by ID", description = "Retrieve a stock transfer by its ID")
    public ApiResponse<StockTransferResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(stockTransferService.getById(id));
    }

    @GetMapping("/from-branch/{fromBranchId}")
    @Operation(summary = "Get stock transfers from branch", description = "Retrieve all stock transfers from a specific branch")
    public ApiResponse<List<StockTransferResponse>> getByFromBranch(@PathVariable Long fromBranchId) {
        return ApiResponse.success(stockTransferService.getByFromBranch(fromBranchId));
    }

    @GetMapping("/to-branch/{toBranchId}")
    @Operation(summary = "Get stock transfers to branch", description = "Retrieve all stock transfers to a specific branch")
    public ApiResponse<List<StockTransferResponse>> getByToBranch(@PathVariable Long toBranchId) {
        return ApiResponse.success(stockTransferService.getByToBranch(toBranchId));
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get stock transfers by branch", description = "Retrieve all stock transfers for a specific branch (from or to)")
    public ApiResponse<List<StockTransferResponse>> getByBranch(@PathVariable Long branchId) {
        return ApiResponse.success(stockTransferService.getByBranch(branchId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get stock transfers by status", description = "Retrieve all stock transfers with a specific status")
    public ApiResponse<List<StockTransferResponse>> getByStatus(@PathVariable TransferStatus status) {
        return ApiResponse.success(stockTransferService.getByStatus(status));
    }

    @GetMapping
    @Operation(summary = "Get all stock transfers", description = "Retrieve all stock transfer records")
    public ApiResponse<List<StockTransferResponse>> getAll() {
        return ApiResponse.success(stockTransferService.getAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete stock transfer", description = "Delete a stock transfer by its ID")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        stockTransferService.delete(id);
        return ApiResponse.success("Stock transfer deleted successfully", null);
    }
}

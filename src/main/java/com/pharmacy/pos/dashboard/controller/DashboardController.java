package com.pharmacy.pos.dashboard.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    @Operation(summary = "Get dashboard overview", description = "Get overall dashboard statistics")
    public ApiResponse<Map<String, Object>> getOverview(
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @Parameter(description = "Branch ID") @RequestParam(required = false) Long branchId) {
        return ApiResponse.success(dashboardService.getOverview(from, to, branchId));
    }

    @GetMapping("/sales")
    @Operation(summary = "Get sales data", description = "Get sales statistics and data")
    public ApiResponse<Map<String, Object>> getSales(
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @Parameter(description = "Branch ID") @RequestParam(required = false) Long branchId) {
        return ApiResponse.success(dashboardService.getSales(from, to, branchId));
    }

    @GetMapping("/products")
    @Operation(summary = "Get products data", description = "Get product statistics")
    public ApiResponse<Map<String, Object>> getProducts(
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @Parameter(description = "Branch ID") @RequestParam(required = false) Long branchId) {
        return ApiResponse.success(dashboardService.getProducts(from, to, branchId));
    }

    @GetMapping("/customers")
    @Operation(summary = "Get customers data", description = "Get customer statistics")
    public ApiResponse<Map<String, Object>> getCustomers(
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @Parameter(description = "Branch ID") @RequestParam(required = false) Long branchId) {
        return ApiResponse.success(dashboardService.getCustomers(from, to, branchId));
    }

    @GetMapping("/orders")
    @Operation(summary = "Get orders data", description = "Get order statistics")
    public ApiResponse<Map<String, Object>> getOrders(
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @Parameter(description = "Branch ID") @RequestParam(required = false) Long branchId) {
        return ApiResponse.success(dashboardService.getOrders(from, to, branchId));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock products", description = "Get products with low stock")
    public ApiResponse<Map<String, Object>> getLowStock(
            @Parameter(description = "Branch ID") @RequestParam(required = false) Long branchId) {
        return ApiResponse.success(dashboardService.getLowStock(branchId));
    }

    @GetMapping("/top-products")
    @Operation(summary = "Get top selling products", description = "Get top selling products")
    public ApiResponse<Map<String, Object>> getTopProducts(
            @Parameter(description = "Limit") @RequestParam(required = false) Integer limit,
            @Parameter(description = "Branch ID") @RequestParam(required = false) Long branchId) {
        return ApiResponse.success(dashboardService.getTopProducts(limit, branchId));
    }

    @GetMapping("/recent-orders")
    @Operation(summary = "Get recent orders", description = "Get recent orders")
    public ApiResponse<Map<String, Object>> getRecentOrders(
            @Parameter(description = "Limit") @RequestParam(required = false) Integer limit,
            @Parameter(description = "Branch ID") @RequestParam(required = false) Long branchId) {
        return ApiResponse.success(dashboardService.getRecentOrders(limit, branchId));
    }

    @GetMapping("/branches")
    @Operation(summary = "Get branch statistics", description = "Get statistics by branch")
    public ApiResponse<Map<String, Object>> getBranches() {
        return ApiResponse.success(dashboardService.getBranches());
    }
}
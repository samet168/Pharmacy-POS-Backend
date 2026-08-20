package com.pharmacy.pos.dashboard.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('report.view') or hasAuthority('ADMIN')")
    @Operation(summary = "Get dashboard overview", description = "Get overall dashboard statistics")
    public ApiResponse<Map<String, Object>> getOverview(
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(dashboardService.getOverview(from, to));
    }

    @GetMapping("/sales")
    @PreAuthorize("hasAuthority('report.view') or hasAuthority('ADMIN')")
    @Operation(summary = "Get sales data", description = "Get sales statistics and data")
    public ApiResponse<Map<String, Object>> getSales(
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(dashboardService.getSales(from, to));
    }

    @GetMapping("/products")
    @PreAuthorize("hasAuthority('report.view') or hasAuthority('ADMIN')")
    @Operation(summary = "Get products data", description = "Get product statistics")
    public ApiResponse<Map<String, Object>> getProducts(
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(dashboardService.getProducts(from, to));
    }

    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('report.view') or hasAuthority('ADMIN')")
    @Operation(summary = "Get customers data", description = "Get customer statistics")
    public ApiResponse<Map<String, Object>> getCustomers(
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(dashboardService.getCustomers(from, to));
    }

    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('report.view') or hasAuthority('ADMIN')")
    @Operation(summary = "Get orders data", description = "Get order statistics")
    public ApiResponse<Map<String, Object>> getOrders(
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(dashboardService.getOrders(from, to));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAuthority('report.view') or hasAuthority('ADMIN')")
    @Operation(summary = "Get low stock products", description = "Get products with low stock")
    public ApiResponse<Map<String, Object>> getLowStock() {
        return ApiResponse.success(dashboardService.getLowStock());
    }

    @GetMapping("/top-products")
    @PreAuthorize("hasAuthority('report.view') or hasAuthority('ADMIN')")
    @Operation(summary = "Get top selling products", description = "Get top selling products")
    public ApiResponse<Map<String, Object>> getTopProducts(
            @Parameter(description = "Limit") @RequestParam(required = false) Integer limit) {
        return ApiResponse.success(dashboardService.getTopProducts(limit));
    }

    @GetMapping("/recent-orders")
    @PreAuthorize("hasAuthority('report.view') or hasAuthority('ADMIN')")
    @Operation(summary = "Get recent orders", description = "Get recent orders")
    public ApiResponse<Map<String, Object>> getRecentOrders(
            @Parameter(description = "Limit") @RequestParam(required = false) Integer limit) {
        return ApiResponse.success(dashboardService.getRecentOrders(limit));
    }

    @GetMapping("/branches")
    @PreAuthorize("hasAuthority('report.view') or hasAuthority('ADMIN')")
    @Operation(summary = "Get branch statistics", description = "Get statistics by branch")
    public ApiResponse<Map<String, Object>> getBranches() {
        return ApiResponse.success(dashboardService.getBranches());
    }
}
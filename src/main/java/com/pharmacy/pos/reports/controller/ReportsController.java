package com.pharmacy.pos.reports.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.reports.dto.*;
import com.pharmacy.pos.reports.service.ReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReportsController {

    private final ReportsService reportsService;

    @GetMapping("/sales")
    @PreAuthorize("hasAuthority('report.view')")
    @Operation(summary = "Get sales report", description = "Get comprehensive sales statistics and breakdown")
    public ApiResponse<SalesReportResponse> getSalesReport(
            @Parameter(description = "Organization ID") @RequestParam Long organizationId,
            @Parameter(description = "Branch ID (optional)") @RequestParam(required = false) Long branchId,
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(reportsService.getSalesReport(organizationId, branchId, from, to));
    }

    @GetMapping("/products")
    @PreAuthorize("hasAuthority('report.view')")
    @Operation(summary = "Get product report", description = "Get product performance statistics")
    public ApiResponse<ProductReportResponse> getProductReport(
            @Parameter(description = "Organization ID") @RequestParam Long organizationId,
            @Parameter(description = "Branch ID (optional)") @RequestParam(required = false) Long branchId,
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(reportsService.getProductReport(organizationId, branchId, from, to));
    }

    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('report.view')")
    @Operation(summary = "Get customer report", description = "Get customer statistics and spending analysis")
    public ApiResponse<CustomerReportResponse> getCustomerReport(
            @Parameter(description = "Organization ID") @RequestParam Long organizationId,
            @Parameter(description = "Branch ID (optional)") @RequestParam(required = false) Long branchId,
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(reportsService.getCustomerReport(organizationId, branchId, from, to));
    }

    @GetMapping("/purchases")
    @PreAuthorize("hasAuthority('report.view')")
    @Operation(summary = "Get purchase report", description = "Get purchase order statistics")
    public ApiResponse<PurchaseReportResponse> getPurchaseReport(
            @Parameter(description = "Organization ID") @RequestParam Long organizationId,
            @Parameter(description = "Branch ID (optional)") @RequestParam(required = false) Long branchId,
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(reportsService.getPurchaseReport(organizationId, branchId, from, to));
    }

    @GetMapping("/inventory")
    @PreAuthorize("hasAuthority('report.view')")
    @Operation(summary = "Get inventory report", description = "Get inventory statistics and stock levels")
    public ApiResponse<InventoryReportResponse> getInventoryReport(
            @Parameter(description = "Organization ID") @RequestParam Long organizationId,
            @Parameter(description = "Branch ID (optional)") @RequestParam(required = false) Long branchId) {
        return ApiResponse.success(reportsService.getInventoryReport(organizationId, branchId));
    }

    @GetMapping("/staff-performance")
    @PreAuthorize("hasAuthority('report.view')")
    @Operation(summary = "Get staff performance report", description = "Get staff performance statistics")
    public ApiResponse<StaffPerformanceResponse> getStaffPerformanceReport(
            @Parameter(description = "Organization ID") @RequestParam Long organizationId,
            @Parameter(description = "Branch ID (optional)") @RequestParam(required = false) Long branchId,
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(reportsService.getStaffPerformanceReport(organizationId, branchId, from, to));
    }
}
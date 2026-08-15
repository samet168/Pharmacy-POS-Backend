package com.pharmacy.pos.iam.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.iam.dto.ShiftRequest;
import com.pharmacy.pos.iam.dto.ShiftResponse;
import com.pharmacy.pos.iam.service.ShiftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    @Operation(summary = "Open shift", description = "Open a new shift")
    public ApiResponse<ShiftResponse> openShift(@Valid @RequestBody ShiftRequest request) {
        return ApiResponse.success(shiftService.openShift(request));
    }

    @PutMapping("/{id}/close")
    @Operation(summary = "Close shift", description = "Close an existing shift")
    public ApiResponse<ShiftResponse> closeShift(@PathVariable Long id, @Valid @RequestBody ShiftRequest request) {
        return ApiResponse.success(shiftService.closeShift(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get shift by ID", description = "Retrieve a shift by its ID")
    public ApiResponse<ShiftResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(shiftService.getById(id));
    }

    @GetMapping("/current")
    @Operation(summary = "Get current shift", description = "Get the current open shift for a user")
    public ApiResponse<ShiftResponse> getCurrentShift(@Parameter(description = "User ID") @RequestParam Long userId) {
        return ApiResponse.success(shiftService.getCurrentShift(userId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get shifts by user", description = "Retrieve all shifts for a specific user")
    public ApiResponse<PageResponse<ShiftResponse>> getByUser(@PathVariable Long userId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(shiftService.getByUser(userId, pageable)));
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get shifts by branch", description = "Retrieve all shifts for a specific branch")
    public ApiResponse<PageResponse<ShiftResponse>> getByBranch(@PathVariable Long branchId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(shiftService.getByBranch(branchId, pageable)));
    }

    @GetMapping
    @Operation(summary = "Get all shifts", description = "Retrieve all shifts")
    public ApiResponse<PageResponse<ShiftResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(shiftService.getAll(pageable)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shift", description = "Delete a shift by its ID")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        shiftService.delete(id);
        return ApiResponse.success("Shift deleted successfully", null);
    }
}

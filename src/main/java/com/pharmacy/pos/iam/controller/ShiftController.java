package com.pharmacy.pos.iam.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.iam.dto.ShiftRequest;
import com.pharmacy.pos.iam.dto.ShiftResponse;
import com.pharmacy.pos.iam.service.ShiftService;
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
    @PreAuthorize("hasAuthority('shift.open')")
    public ApiResponse<ShiftResponse> openShift(@Valid @RequestBody ShiftRequest request) {
        return ApiResponse.success(shiftService.openShift(request));
    }

    @PutMapping("/{id}/close")
    @PreAuthorize("hasAuthority('shift.close')")
    public ApiResponse<ShiftResponse> closeShift(@PathVariable Long id, @Valid @RequestBody ShiftRequest request) {
        return ApiResponse.success(shiftService.closeShift(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('shift.view')")
    public ApiResponse<ShiftResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(shiftService.getById(id));
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasAuthority('shift.view')")
    public ApiResponse<PageResponse<ShiftResponse>> getByBranch(@PathVariable Long branchId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(shiftService.getByBranch(branchId, pageable)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('shift.view')")
    public ApiResponse<PageResponse<ShiftResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(shiftService.getAll(pageable)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('shift.delete')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        shiftService.delete(id);
        return ApiResponse.success("Shift deleted successfully", null);
    }
}

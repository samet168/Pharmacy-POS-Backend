package com.pharmacy.pos.branch.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.branch.dto.DeviceRequest;
import com.pharmacy.pos.branch.dto.DeviceResponse;
import com.pharmacy.pos.branch.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    @PreAuthorize("hasAuthority('device.create')")
    public ApiResponse<DeviceResponse> create(@Valid @RequestBody DeviceRequest request) {
        return ApiResponse.success(deviceService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('device.update')")
    public ApiResponse<DeviceResponse> update(@PathVariable Long id, @Valid @RequestBody DeviceRequest request) {
        return ApiResponse.success(deviceService.update(id, request));
    }

    @PostMapping("/sync/{deviceUuid}")
    public ApiResponse<DeviceResponse> updateLastSynced(@PathVariable String deviceUuid) {
        return ApiResponse.success(deviceService.updateLastSynced(deviceUuid));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('device.view')")
    public ApiResponse<DeviceResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(deviceService.getById(id));
    }

    @GetMapping("/uuid/{deviceUuid}")
    public ApiResponse<DeviceResponse> getByDeviceUuid(@PathVariable String deviceUuid) {
        return ApiResponse.success(deviceService.getByDeviceUuid(deviceUuid));
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasAuthority('device.view')")
    public ApiResponse<PageResponse<DeviceResponse>> getByBranch(@PathVariable Long branchId, Pageable pageable) {
        return ApiResponse.success(PageResponse.of(deviceService.getByBranch(branchId, pageable)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('device.view')")
    public ApiResponse<PageResponse<DeviceResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(deviceService.getAll(pageable)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('device.delete')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        deviceService.delete(id);
        return ApiResponse.success("Device deleted successfully", null);
    }
}

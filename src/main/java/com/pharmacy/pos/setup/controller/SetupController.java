package com.pharmacy.pos.setup.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.iam.dto.LoginResponse;
import com.pharmacy.pos.setup.dto.BootstrapRequest;
import com.pharmacy.pos.setup.dto.CreateAdminRequest;
import com.pharmacy.pos.setup.service.SetupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/setup")
@RequiredArgsConstructor
public class SetupController {

    private final SetupService setupService;

    @PostMapping("/bootstrap")
    public ApiResponse<LoginResponse> bootstrap(@Valid @RequestBody BootstrapRequest request) {
        return ApiResponse.success(setupService.bootstrap(request));
    }

    @PostMapping("/fix-permissions")
    public ApiResponse<String> fixPermissions() {
        return ApiResponse.success(setupService.fixPermissions());
    }

    @PostMapping("/create-admin")
    public ApiResponse<LoginResponse> createAdmin(@Valid @RequestBody CreateAdminRequest request) {
        return ApiResponse.success(setupService.createAdmin(request));
    }
}
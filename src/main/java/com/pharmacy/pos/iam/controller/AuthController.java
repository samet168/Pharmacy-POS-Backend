package com.pharmacy.pos.iam.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.iam.dto.LoginRequest;
import com.pharmacy.pos.iam.dto.LoginResponse;
import com.pharmacy.pos.iam.dto.PinLoginRequest;
import com.pharmacy.pos.iam.dto.RefreshTokenRequest;
import com.pharmacy.pos.iam.dto.RegisterRequest;
import com.pharmacy.pos.iam.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success(authService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.success(authService.refreshToken(request));
    }

    @PostMapping("/pin-login")
    public ApiResponse<LoginResponse> pinLogin(@Valid @RequestBody PinLoginRequest request) {
        return ApiResponse.success(authService.pinLogin(request));
    }
}

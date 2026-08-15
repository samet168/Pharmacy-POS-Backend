package com.pharmacy.pos.iam.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.iam.dto.ChangePasswordRequest;
import com.pharmacy.pos.iam.dto.LoginRequest;
import com.pharmacy.pos.iam.dto.LoginResponse;
import com.pharmacy.pos.iam.dto.PinLoginRequest;
import com.pharmacy.pos.iam.dto.RefreshTokenRequest;
import com.pharmacy.pos.iam.dto.RegisterRequest;
import com.pharmacy.pos.iam.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@SecurityRequirements // Excludes global security requirement for auth endpoints
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

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> getCurrentUser(Authentication authentication) {
        com.pharmacy.pos.security.CustomUserDetails userDetails = 
            (com.pharmacy.pos.security.CustomUserDetails) authentication.getPrincipal();
        com.pharmacy.pos.iam.entity.User user = userDetails.getUser();
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("username", user.getUsername());
        userData.put("name", user.getName());
        userData.put("phone", user.getPhone());
        userData.put("imageUrl", user.getImageUrl());
        userData.put("active", user.isActive());
        userData.put("organizationId", user.getOrganization() != null ? user.getOrganization().getId() : null);
        userData.put("roleId", user.getRole() != null ? user.getRole().getId() : null);
        userData.put("roleName", user.getRole() != null ? user.getRole().getName() : null);
        userData.put("authorities", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        userData.put("authenticated", authentication.isAuthenticated());
        
        return ApiResponse.success(userData);
    }

    @PutMapping("/change-password")
    public ApiResponse<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        // Extract userId from the CustomUserDetails
        com.pharmacy.pos.security.CustomUserDetails userDetails = 
            (com.pharmacy.pos.security.CustomUserDetails) authentication.getPrincipal();
        authService.changePassword(userDetails.getUser().getId(), request);
        return ApiResponse.success("Password changed successfully", null);
    }
}

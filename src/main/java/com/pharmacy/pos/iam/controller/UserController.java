package com.pharmacy.pos.iam.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.iam.dto.UserRequest;
import com.pharmacy.pos.iam.dto.UserResponse;
import com.pharmacy.pos.iam.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create user", description = "Create a new user with optional image upload")
    public ApiResponse<UserResponse> create(
            @Parameter(description = "User data as JSON", required = true, content = @Content(schema = @Schema(implementation = UserRequest.class))) @RequestPart(value = "user", required = true) @Valid UserRequest request,
            @Parameter(description = "Image file (optional)") @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            return ApiResponse.success(userService.createWithImage(request, file));
        }
        return ApiResponse.success(userService.create(request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update user", description = "Update user with optional image upload")
    public ApiResponse<UserResponse> update(
            @PathVariable Long id,
            @Parameter(description = "User data as JSON", required = true, content = @Content(schema = @Schema(implementation = UserRequest.class))) @RequestPart(value = "user", required = true) @Valid UserRequest request,
            @Parameter(description = "Image file (optional)") @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            return ApiResponse.success(userService.updateWithImage(id, request, file));
        }
        return ApiResponse.success(userService.update(id, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(userService.getById(id));
    }

    @GetMapping
    public ApiResponse<PageResponse<UserResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(userService.getAll(pageable)));
    }

    @GetMapping("/organization/{organizationId}")
    public ApiResponse<PageResponse<UserResponse>> getByOrganization(
            @PathVariable Long organizationId,
            Pageable pageable) {
        return ApiResponse.success(PageResponse.of(userService.getByOrganization(organizationId, pageable)));
    }

    @PutMapping("/{id}/reset-password")
    public ApiResponse<Void> resetPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String newPassword = body.getOrDefault("password", "123456");
        userService.resetPassword(id, newPassword);
        return ApiResponse.success("Password reset successfully", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.success("User deleted successfully", null);
    }
}

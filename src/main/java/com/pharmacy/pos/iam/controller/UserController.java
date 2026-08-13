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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('user.create')")
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
    @PreAuthorize("hasAuthority('user.update')")
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
    @PreAuthorize("hasAuthority('user.view')")
    public ApiResponse<UserResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(userService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user.view')")
    public ApiResponse<PageResponse<UserResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(userService.getAll(pageable)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user.delete')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.success("User deleted successfully", null);
    }
}

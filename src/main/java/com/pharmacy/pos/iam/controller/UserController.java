package com.pharmacy.pos.iam.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.iam.dto.UserRequest;
import com.pharmacy.pos.iam.dto.UserResponse;
import com.pharmacy.pos.iam.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAuthority('user.create')")
    public ApiResponse<UserResponse> create(@Valid @RequestBody UserRequest request) {
        return ApiResponse.success(userService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user.update')")
    public ApiResponse<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
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

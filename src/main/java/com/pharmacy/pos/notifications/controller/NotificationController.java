package com.pharmacy.pos.notifications.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.notifications.dto.NotificationRequest;
import com.pharmacy.pos.notifications.dto.NotificationResponse;
import com.pharmacy.pos.notifications.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Create notification", description = "Create a new notification")
    public ApiResponse<NotificationResponse> create(@Valid @RequestBody NotificationRequest request) {
        return ApiResponse.success(notificationService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID", description = "Get a specific notification by ID")
    public ApiResponse<NotificationResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(notificationService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get user notifications", description = "Get notifications for the current user")
    public ApiResponse<PageResponse<NotificationResponse>> getUserNotifications(
            @RequestParam Long userId,
            Pageable pageable) {
        return ApiResponse.success(PageResponse.of(notificationService.getByUserId(userId, pageable)));
    }

    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Get organization notifications", description = "Get notifications for an organization")
    public ApiResponse<PageResponse<NotificationResponse>> getOrganizationNotifications(
            @PathVariable Long organizationId,
            Pageable pageable) {
        return ApiResponse.success(PageResponse.of(notificationService.getByOrganizationId(organizationId, pageable)));
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Get unread count", description = "Get count of unread notifications for a user")
    public ApiResponse<Long> getUnreadCount(@RequestParam Long userId) {
        return ApiResponse.success(notificationService.getUnreadCount(userId));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark notification as read", description = "Mark a notification as read")
    public ApiResponse<NotificationResponse> markAsRead(@PathVariable Long id) {
        return ApiResponse.success(notificationService.markAsRead(id));
    }

    @PutMapping("/read-all")
    @Operation(summary = "Mark all notifications as read", description = "Mark all notifications as read for a user")
    public ApiResponse<Void> markAllAsRead(@RequestParam Long userId) {
        notificationService.markAllAsRead(userId);
        return ApiResponse.success("All notifications marked as read", null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification", description = "Delete a notification")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        notificationService.delete(id);
        return ApiResponse.success("Notification deleted successfully", null);
    }
}
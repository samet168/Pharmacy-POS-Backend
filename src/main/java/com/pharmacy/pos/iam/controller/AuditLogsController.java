package com.pharmacy.pos.iam.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.iam.dto.AuditLogRequest;
import com.pharmacy.pos.iam.dto.AuditLogResponse;
import com.pharmacy.pos.iam.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Audit Logs", description = "Endpoints for managing and querying system audit logs")
public class AuditLogsController {

    private final AuditLogService auditLogService;

    @PostMapping
    @Operation(summary = "Create audit log", description = "Create a new audit log entry")
    public ApiResponse<AuditLogResponse> create(@Valid @RequestBody AuditLogRequest request) {
        return ApiResponse.success(auditLogService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update audit log", description = "Update an existing audit log entry")
    public ApiResponse<AuditLogResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AuditLogRequest request) {
        return ApiResponse.success(auditLogService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get audit log by ID", description = "Retrieve an audit log entry by its ID")
    public ApiResponse<AuditLogResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(auditLogService.getById(id));
    }

    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Get audit logs by organization", description = "Retrieve all audit logs for a specific organization")
    public ApiResponse<List<AuditLogResponse>> getByOrganization(@PathVariable Long organizationId) {
        return ApiResponse.success(auditLogService.getByOrganization(organizationId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get audit logs by user", description = "Retrieve all audit logs for a specific user")
    public ApiResponse<List<AuditLogResponse>> getByUser(@PathVariable Long userId) {
        return ApiResponse.success(auditLogService.getByUser(userId));
    }

    @GetMapping("/action/{action}")
    @Operation(summary = "Get audit logs by action", description = "Retrieve all audit logs for a specific action")
    public ApiResponse<List<AuditLogResponse>> getByAction(@PathVariable String action) {
        return ApiResponse.success(auditLogService.getByAction(action));
    }

    @GetMapping("/entity-type/{entityType}")
    @Operation(summary = "Get audit logs by entity type", description = "Retrieve all audit logs for a specific entity type")
    public ApiResponse<List<AuditLogResponse>> getByEntityType(@PathVariable String entityType) {
        return ApiResponse.success(auditLogService.getByEntityType(entityType));
    }

    @GetMapping("/organization/{organizationId}/date-range")
    @Operation(summary = "Get audit logs by organization and date range", description = "Retrieve audit logs for an organization within a date range")
    public ApiResponse<List<AuditLogResponse>> getByOrganizationAndDateRange(
            @PathVariable Long organizationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ApiResponse.success(auditLogService.getByOrganizationAndDateRange(organizationId, startDate, endDate));
    }

    @GetMapping("/user/{userId}/date-range")
    @Operation(summary = "Get audit logs by user and date range", description = "Retrieve audit logs for a user within a date range")
    public ApiResponse<List<AuditLogResponse>> getByUserAndDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ApiResponse.success(auditLogService.getByUserAndDateRange(userId, startDate, endDate));
    }

    @GetMapping
    @Operation(summary = "Get all audit logs with optional query params", description = "Retrieve all audit log entries with optional filters")
    public ApiResponse<List<AuditLogResponse>> getAll(
            @RequestParam(required = false) Long organizationId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        if (organizationId != null && from != null && to != null) {
            LocalDateTime start = from.atStartOfDay();
            LocalDateTime end = to.atTime(LocalTime.MAX);
            return ApiResponse.success(auditLogService.getByOrganizationAndDateRange(organizationId, start, end));
        }

        if (organizationId != null) {
            return ApiResponse.success(auditLogService.getByOrganization(organizationId));
        }

        if (userId != null) {
            return ApiResponse.success(auditLogService.getByUser(userId));
        }

        if (action != null && !action.isBlank()) {
            return ApiResponse.success(auditLogService.getByAction(action));
        }

        if (entityType != null && !entityType.isBlank()) {
            return ApiResponse.success(auditLogService.getByEntityType(entityType));
        }

        return ApiResponse.success(auditLogService.getAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete audit log", description = "Delete an audit log entry by its ID")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        auditLogService.delete(id);
        return ApiResponse.success("Audit log deleted successfully", null);
    }
}

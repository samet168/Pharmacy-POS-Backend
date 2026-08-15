package com.pharmacy.pos.iam.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuditLogRequest {
    private Long organizationId;

    private Long userId;

    private String username;

    @NotBlank
    private String action;

    private String entityType;

    private Long entityId;

    private String description;

    private String ipAddress;

    private String userAgent;

    private String requestMethod;

    private String requestUrl;

    private String requestBody;

    private String responseBody;

    private Integer statusCode;

    private Long executionTimeMs;
}

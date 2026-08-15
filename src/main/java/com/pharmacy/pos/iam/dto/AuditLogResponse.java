package com.pharmacy.pos.iam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {
    private Long id;
    private Long organizationId;
    private Long userId;
    private String username;
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
    private LocalDateTime createdAt;
}

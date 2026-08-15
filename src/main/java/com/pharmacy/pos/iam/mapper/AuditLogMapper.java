package com.pharmacy.pos.iam.mapper;

import com.pharmacy.pos.iam.dto.AuditLogRequest;
import com.pharmacy.pos.iam.dto.AuditLogResponse;
import com.pharmacy.pos.iam.entity.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

    public AuditLog toEntity(AuditLogRequest request) {
        AuditLog auditLog = new AuditLog();
        auditLog.setOrganizationId(request.getOrganizationId());
        auditLog.setUserId(request.getUserId());
        auditLog.setUsername(request.getUsername());
        auditLog.setAction(request.getAction());
        auditLog.setEntityType(request.getEntityType());
        auditLog.setEntityId(request.getEntityId());
        auditLog.setDescription(request.getDescription());
        auditLog.setIpAddress(request.getIpAddress());
        auditLog.setUserAgent(request.getUserAgent());
        auditLog.setRequestMethod(request.getRequestMethod());
        auditLog.setRequestUrl(request.getRequestUrl());
        auditLog.setRequestBody(request.getRequestBody());
        auditLog.setResponseBody(request.getResponseBody());
        auditLog.setStatusCode(request.getStatusCode());
        auditLog.setExecutionTimeMs(request.getExecutionTimeMs());
        return auditLog;
    }

    public AuditLogResponse toResponse(AuditLog auditLog) {
        AuditLogResponse response = new AuditLogResponse();
        response.setId(auditLog.getId());
        response.setOrganizationId(auditLog.getOrganizationId());
        response.setUserId(auditLog.getUserId());
        response.setUsername(auditLog.getUsername());
        response.setAction(auditLog.getAction());
        response.setEntityType(auditLog.getEntityType());
        response.setEntityId(auditLog.getEntityId());
        response.setDescription(auditLog.getDescription());
        response.setIpAddress(auditLog.getIpAddress());
        response.setUserAgent(auditLog.getUserAgent());
        response.setRequestMethod(auditLog.getRequestMethod());
        response.setRequestUrl(auditLog.getRequestUrl());
        response.setRequestBody(auditLog.getRequestBody());
        response.setResponseBody(auditLog.getResponseBody());
        response.setStatusCode(auditLog.getStatusCode());
        response.setExecutionTimeMs(auditLog.getExecutionTimeMs());
        response.setCreatedAt(auditLog.getCreatedAt());
        return response;
    }

    public void updateEntityFromRequest(AuditLog auditLog, AuditLogRequest request) {
        auditLog.setOrganizationId(request.getOrganizationId());
        auditLog.setUserId(request.getUserId());
        auditLog.setUsername(request.getUsername());
        auditLog.setAction(request.getAction());
        auditLog.setEntityType(request.getEntityType());
        auditLog.setEntityId(request.getEntityId());
        auditLog.setDescription(request.getDescription());
        auditLog.setIpAddress(request.getIpAddress());
        auditLog.setUserAgent(request.getUserAgent());
        auditLog.setRequestMethod(request.getRequestMethod());
        auditLog.setRequestUrl(request.getRequestUrl());
        auditLog.setRequestBody(request.getRequestBody());
        auditLog.setResponseBody(request.getResponseBody());
        auditLog.setStatusCode(request.getStatusCode());
        auditLog.setExecutionTimeMs(request.getExecutionTimeMs());
    }
}

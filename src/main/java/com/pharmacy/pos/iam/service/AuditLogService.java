package com.pharmacy.pos.iam.service;

import com.pharmacy.pos.iam.dto.AuditLogRequest;
import com.pharmacy.pos.iam.dto.AuditLogResponse;
import com.pharmacy.pos.iam.entity.AuditLog;
import com.pharmacy.pos.iam.mapper.AuditLogMapper;
import com.pharmacy.pos.iam.repository.AuditLogRepository;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public List<AuditLogResponse> getAll() {
        return auditLogRepository.findAll().stream()
                .map(auditLogMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getByOrganization(Long organizationId) {
        return auditLogRepository.findByOrganizationId(organizationId).stream()
                .map(auditLogMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getByUser(Long userId) {
        return auditLogRepository.findByUserId(userId).stream()
                .map(auditLogMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getByAction(String action) {
        return auditLogRepository.findByAction(action).stream()
                .map(auditLogMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getByEntityType(String entityType) {
        return auditLogRepository.findByEntityType(entityType).stream()
                .map(auditLogMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getByOrganizationAndDateRange(
            Long organizationId, LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByOrganizationIdAndCreatedAtBetween(organizationId, startDate, endDate).stream()
                .map(auditLogMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getByUserAndDateRange(
            Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByUserIdAndCreatedAtBetween(userId, startDate, endDate).stream()
                .map(auditLogMapper::toResponse)
                .collect(Collectors.toList());
    }

    public AuditLogResponse getById(Long id) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit log not found with id: " + id));
        return auditLogMapper.toResponse(auditLog);
    }

    @Transactional
    public AuditLogResponse create(AuditLogRequest request) {
        AuditLog auditLog = auditLogMapper.toEntity(request);
        AuditLog saved = auditLogRepository.save(auditLog);
        return auditLogMapper.toResponse(saved);
    }

    @Transactional
    public AuditLogResponse update(Long id, AuditLogRequest request) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit log not found with id: " + id));

        auditLogMapper.updateEntityFromRequest(auditLog, request);
        AuditLog updated = auditLogRepository.save(auditLog);
        return auditLogMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit log not found with id: " + id));
        auditLogRepository.delete(auditLog);
    }
}

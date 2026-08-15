package com.pharmacy.pos.iam.repository;

import com.pharmacy.pos.iam.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByOrganizationId(Long organizationId);

    List<AuditLog> findByUserId(Long userId);

    List<AuditLog> findByAction(String action);

    List<AuditLog> findByEntityType(String entityType);

    List<AuditLog> findByOrganizationIdAndCreatedAtBetween(
            Long organizationId, LocalDateTime startDate, LocalDateTime endDate);

    List<AuditLog> findByUserIdAndCreatedAtBetween(
            Long userId, LocalDateTime startDate, LocalDateTime endDate);
}

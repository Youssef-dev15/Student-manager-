package org.example.student_management_system.Repository;

import org.example.student_management_system.entities.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    Page<AuditLog> findByUserUserId(UUID userId, Pageable pageable);

    Page<AuditLog> findByActionType(String actionType, Pageable pageable);

    Page<AuditLog> findByTimestampAfter(LocalDateTime date, Pageable pageable);
}
package org.example.student_management_system.services;

import org.example.student_management_system.Dto.AuditLogRequestDto;
import org.example.student_management_system.Dto.AuditLogResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IAuditLogService {
    AuditLogResponseDto addAuditLog(AuditLogRequestDto dto);
    Page<AuditLogResponseDto> getAllAuditLogs(Pageable pageable);
    Page<AuditLogResponseDto> getAuditLogsByUserId(UUID userId, Pageable pageable);
    Page<AuditLogResponseDto> getAuditLogsByActionType(String actionType, Pageable pageable);
    Page<AuditLogResponseDto> getAuditLogsAfterDate(LocalDateTime date, Pageable pageable);
}
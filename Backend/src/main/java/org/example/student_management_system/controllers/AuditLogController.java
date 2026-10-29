package org.example.student_management_system.controllers;

import lombok.RequiredArgsConstructor;
import org.example.student_management_system.Dto.AuditLogResponseDto;
import org.example.student_management_system.services.IAuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
// TODO(security): @PreAuthorize("hasRole('ADMIN')")
public class AuditLogController {

    private final IAuditLogService auditLogService;

    @GetMapping
    public Page<AuditLogResponseDto> getAll(
            @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return auditLogService.getAllAuditLogs(pageable);
    }

    @GetMapping("/user/{userId}")
    public Page<AuditLogResponseDto> getByUser(
            @PathVariable UUID userId,
            @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return auditLogService.getAuditLogsByUserId(userId, pageable);
    }

    @GetMapping("/action/{actionType}")
    public Page<AuditLogResponseDto> getByAction(
            @PathVariable String actionType,
            @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return auditLogService.getAuditLogsByActionType(actionType, pageable);
    }

    @GetMapping("/after")
    public Page<AuditLogResponseDto> getAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return auditLogService.getAuditLogsAfterDate(date, pageable);
    }
}
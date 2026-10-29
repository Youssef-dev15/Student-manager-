package org.example.student_management_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.student_management_system.entities.AuditLog;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponseDto {
    private UUID logId;
    private String actionType;
    private String description;
    private LocalDateTime timestamp;
    private UUID userId;
    private String userEmail;

    public static AuditLogResponseDto fromAuditLogToAuditLogResponseDto(AuditLog log) {
        return AuditLogResponseDto.builder()
                .logId(log.getLogId())
                .actionType(log.getActionType())
                .description(log.getDescription())
                .timestamp(log.getTimestamp())
                .userId(log.getUser() != null ? log.getUser().getUserId() : null)
                .userEmail(log.getUser() != null ? log.getUser().getEmail() : null)
                .build();
    }
}
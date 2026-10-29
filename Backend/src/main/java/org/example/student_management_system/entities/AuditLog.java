package org.example.student_management_system.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.student_management_system.Dto.AuditLogRequestDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID logId;

    private String adminEmail;
    private String actionType;
    private String description;
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static AuditLog fromAuditLogRequestDtoToAuditLog(AuditLogRequestDto dto, User user) {
        return AuditLog.builder()
                .actionType(dto.getActionType())
                .description(dto.getDescription())
                .user(user)
                .build();
    }
}
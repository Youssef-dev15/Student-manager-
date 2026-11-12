package org.example.student_management_system.Repository;

import org.example.student_management_system.entities.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;
import java.time.LocalDateTime;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    Page<AuditLog> findByUserUserId(UUID userId, Pageable pageable);

    Page<AuditLog> findByActionType(String actionType, Pageable pageable);

    Page<AuditLog> findByTimestampAfter(LocalDateTime date, Pageable pageable);

    @Modifying
    @Query(value = """
        INSERT INTO audit_logs (log_id, action_type, admin_email, description, timestamp, user_id)
        VALUES (gen_random_uuid(), :actionType, :email, :description, now(), :userId)
        """, nativeQuery = true)
    int insertEntry(@Param("actionType") String actionType,
                    @Param("email") String email,
                    @Param("description") String description,
                    @Param("userId") UUID userId);
}
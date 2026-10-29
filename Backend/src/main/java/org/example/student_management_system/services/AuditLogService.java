package org.example.student_management_system.services;

import lombok.RequiredArgsConstructor;
import org.example.student_management_system.Dto.AuditLogRequestDto;
import org.example.student_management_system.Dto.AuditLogResponseDto;
import org.example.student_management_system.Repository.AuditLogRepository;
import org.example.student_management_system.Repository.UserRepository;
import org.example.student_management_system.entities.AuditLog;
import org.example.student_management_system.entities.User;
import org.example.student_management_system.services.IAuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogService implements IAuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AuditLogResponseDto addAuditLog(AuditLogRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found with ID : " + dto.getUserId()));

        AuditLog log = AuditLog.fromAuditLogRequestDtoToAuditLog(dto, user);
        return AuditLogResponseDto.fromAuditLogToAuditLogResponseDto(auditLogRepository.save(log));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponseDto> getAllAuditLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable)
                .map(AuditLogResponseDto::fromAuditLogToAuditLogResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponseDto> getAuditLogsByUserId(UUID userId, Pageable pageable) {
        return auditLogRepository.findByUserUserId(userId, pageable)
                .map(AuditLogResponseDto::fromAuditLogToAuditLogResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponseDto> getAuditLogsByActionType(String actionType, Pageable pageable) {
        return auditLogRepository.findByActionType(actionType, pageable)
                .map(AuditLogResponseDto::fromAuditLogToAuditLogResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponseDto> getAuditLogsAfterDate(LocalDateTime date, Pageable pageable) {
        return auditLogRepository.findByTimestampAfter(date, pageable)
                .map(AuditLogResponseDto::fromAuditLogToAuditLogResponseDto);
    }
}
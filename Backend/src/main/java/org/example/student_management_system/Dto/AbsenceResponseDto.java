package org.example.student_management_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.student_management_system.entities.Absence;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbsenceResponseDto {
    private UUID absenceId;
    private UUID studentId;
    private LocalDate absenceDate;
    private boolean justified;
    private String recordedBy;
    private LocalDateTime createdAt;

    public static AbsenceResponseDto fromAbsenceToAbsenceResponseDto(Absence absence) {
        return AbsenceResponseDto.builder()
                .absenceId(absence.getAbsenceId())
                .studentId(absence.getStudent() != null ? absence.getStudent().getStudentId() : null)
                .absenceDate(absence.getAbsenceDate())
                .justified(absence.isJustified())
                .recordedBy(absence.getRecordedBy())
                .createdAt(absence.getCreatedAt())
                .build();
    }
}
package org.example.student_management_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbsenceRequestDto {
    private UUID studentId;
    private LocalDate absenceDate;
    private boolean justified;
    private String recordedBy;
}
package org.example.student_management_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeReportRequestDto {
    private String academicYear;
    private Integer semesterNumber;
    private String bulletinPdf;
    private UUID studentId; // Pour lier le bulletin à l'étudiant via son ID
}
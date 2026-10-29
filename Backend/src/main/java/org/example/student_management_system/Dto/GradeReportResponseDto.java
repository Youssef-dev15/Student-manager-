package org.example.student_management_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.student_management_system.entities.GradeReport;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeReportResponseDto {
    private UUID reportId;
    private String academicYear;
    private Integer semesterNumber;
    private String bulletinPdf;
    private UUID studentId;

    public static GradeReportResponseDto fromGradeReportToGradeReportResponseDto(GradeReport report) {
        return GradeReportResponseDto.builder()
                .reportId(report.getReportId())
                .academicYear(report.getAcademicYear())
                .semesterNumber(report.getSemesterNumber())
                .bulletinPdf(report.getBulletinPdf())
                .studentId(report.getStudent() != null ? report.getStudent().getStudentId() : null)
                .build();
    }
}
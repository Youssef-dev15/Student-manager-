package org.example.student_management_system.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.student_management_system.Dto.GradeReportRequestDto;

import java.util.UUID;

@Entity
@Table(name = "grade_reports")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reportId;

    private String academicYear;
    private Integer semesterNumber;
    private String bulletinPdf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public static GradeReport fromGradeReportRequestDtoToGradeReport(GradeReportRequestDto dto, Student student) {
        return GradeReport.builder()
                .academicYear(dto.getAcademicYear())
                .semesterNumber(dto.getSemesterNumber())
                .bulletinPdf(dto.getBulletinPdf())
                .student(student)
                .build();
    }
}
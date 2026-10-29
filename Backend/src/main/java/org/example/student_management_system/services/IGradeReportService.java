package org.example.student_management_system.services;

import org.example.student_management_system.Dto.GradeReportRequestDto;
import org.example.student_management_system.Dto.GradeReportResponseDto;

import java.util.List;
import java.util.UUID;

public interface IGradeReportService {
    GradeReportResponseDto addGradeReport(GradeReportRequestDto dto);
    GradeReportResponseDto updateGradeReport(GradeReportRequestDto dto, UUID reportId);
    GradeReportResponseDto getGradeReportById(UUID reportId);
    List<GradeReportResponseDto> getGradeReportsByStudentId(UUID studentId);
    void deleteGradeReport(UUID reportId);
}
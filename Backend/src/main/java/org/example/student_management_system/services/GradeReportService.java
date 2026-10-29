package org.example.student_management_system.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.student_management_system.Dto.GradeReportRequestDto;
import org.example.student_management_system.Dto.GradeReportResponseDto;
import org.example.student_management_system.Repository.GradeReportRepository;
import org.example.student_management_system.Repository.StudentRepository;
import org.example.student_management_system.entities.GradeReport;
import org.example.student_management_system.entities.Student;
import org.example.student_management_system.services.IGradeReportService;
import org.example.student_management_system.services.IStudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GradeReportService implements IGradeReportService {

    private final GradeReportRepository gradeReportRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public GradeReportResponseDto addGradeReport(GradeReportRequestDto dto) {
        Student student = findStudentOrThrow(dto.getStudentId());
        GradeReport report = GradeReport.fromGradeReportRequestDtoToGradeReport(dto, student);
        return GradeReportResponseDto.fromGradeReportToGradeReportResponseDto(
                gradeReportRepository.save(report));
    }

    @Override
    @Transactional
    public GradeReportResponseDto updateGradeReport(GradeReportRequestDto dto, UUID reportId) {
        GradeReport report = findReportOrThrow(reportId);
        // Adapte aux champs réels de GradeReport (le rattachement à l'étudiant ne change pas)
        // report.setAcademicYear(dto.getAcademicYear());
        // report.setSemester(dto.getSemester());
        // report.setAverage(dto.getAverage());
        // report.setReportPdf(dto.getReportPdf());
        return GradeReportResponseDto.fromGradeReportToGradeReportResponseDto(
                gradeReportRepository.save(report));
    }

    @Override
    @Transactional(readOnly = true)
    public GradeReportResponseDto getGradeReportById(UUID reportId) {
        return GradeReportResponseDto.fromGradeReportToGradeReportResponseDto(findReportOrThrow(reportId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeReportResponseDto> getGradeReportsByStudentId(UUID studentId) {
        findStudentOrThrow(studentId);  // 404 clair si l'étudiant n'existe pas
        return gradeReportRepository.findByStudentStudentId(studentId).stream()
                .map(GradeReportResponseDto::fromGradeReportToGradeReportResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteGradeReport(UUID reportId) {
        gradeReportRepository.delete(findReportOrThrow(reportId));
    }

    private GradeReport findReportOrThrow(UUID reportId) {
        return gradeReportRepository.findById(reportId)
                .orElseThrow(() -> new NoSuchElementException("Grade report not found with ID : " + reportId));
    }

    private Student findStudentOrThrow(UUID studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found with ID : " + studentId));
    }
}
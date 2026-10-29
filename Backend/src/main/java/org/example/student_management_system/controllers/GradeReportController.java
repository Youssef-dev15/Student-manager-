package org.example.student_management_system.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.student_management_system.Dto.GradeReportRequestDto;
import org.example.student_management_system.Dto.GradeReportResponseDto;
import org.example.student_management_system.services.IGradeReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
// TODO(security): @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class GradeReportController {

    private final IGradeReportService gradeReportService;

    @PostMapping("/grade-reports")
    public ResponseEntity<GradeReportResponseDto> create(
            @Valid @RequestBody GradeReportRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gradeReportService.addGradeReport(dto));
    }

    @PutMapping("/grade-reports/{id}")
    public GradeReportResponseDto update(@PathVariable UUID id,
                                         @Valid @RequestBody GradeReportRequestDto dto) {
        return gradeReportService.updateGradeReport(dto, id);
    }

    @GetMapping("/grade-reports/{id}")
    public GradeReportResponseDto getById(@PathVariable UUID id) {
        return gradeReportService.getGradeReportById(id);
    }

    @GetMapping("/students/{studentId}/grade-reports")
    public List<GradeReportResponseDto> getByStudent(@PathVariable UUID studentId) {
        return gradeReportService.getGradeReportsByStudentId(studentId);
    }

    @DeleteMapping("/grade-reports/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        gradeReportService.deleteGradeReport(id);
        return ResponseEntity.noContent().build();
    }
}
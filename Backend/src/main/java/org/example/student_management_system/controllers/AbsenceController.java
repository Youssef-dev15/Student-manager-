package org.example.student_management_system.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.student_management_system.Dto.AbsenceRequestDto;
import org.example.student_management_system.Dto.AbsenceResponseDto;
import org.example.student_management_system.services.IAbsenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
// TODO(security): @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class AbsenceController {

    private final IAbsenceService absenceService;

    @PostMapping("/absences")
    public ResponseEntity<AbsenceResponseDto> create(@Valid @RequestBody AbsenceRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(absenceService.addAbsence(dto));
    }

    @PutMapping("/absences/{id}")
    public AbsenceResponseDto update(@PathVariable UUID id, @Valid @RequestBody AbsenceRequestDto dto) {
        return absenceService.updateAbsence(dto, id);
    }

    @GetMapping("/absences/{id}")
    public AbsenceResponseDto getById(@PathVariable UUID id) {
        return absenceService.getAbsenceById(id);
    }

    @GetMapping("/students/{studentId}/absences")
    public List<AbsenceResponseDto> getByStudent(@PathVariable UUID studentId) {
        return absenceService.getAbsencesByStudentId(studentId);
    }

    @DeleteMapping("/absences/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        absenceService.deleteAbsence(id);
        return ResponseEntity.noContent().build();
    }
}
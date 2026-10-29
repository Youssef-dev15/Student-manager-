package org.example.student_management_system.services;

import org.example.student_management_system.Dto.AbsenceRequestDto;
import org.example.student_management_system.Dto.AbsenceResponseDto;

import java.util.List;
import java.util.UUID;

public interface IAbsenceService {
    AbsenceResponseDto addAbsence(AbsenceRequestDto dto);
    AbsenceResponseDto updateAbsence(AbsenceRequestDto dto, UUID absenceId);
    AbsenceResponseDto getAbsenceById(UUID absenceId);
    List<AbsenceResponseDto> getAbsencesByStudentId(UUID studentId);
    void deleteAbsence(UUID absenceId);
}
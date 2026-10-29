package org.example.student_management_system.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.student_management_system.Dto.AbsenceRequestDto;
import org.example.student_management_system.Dto.AbsenceResponseDto;
import org.example.student_management_system.Repository.AbsenceRepository;
import org.example.student_management_system.Repository.StudentRepository;
import org.example.student_management_system.entities.Absence;
import org.example.student_management_system.entities.Student;
import org.example.student_management_system.services.IAbsenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AbsenceService implements IAbsenceService {

    private final AbsenceRepository absenceRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public AbsenceResponseDto addAbsence(AbsenceRequestDto dto) {
        Student student = findStudentOrThrow(dto.getStudentId());

        Absence absence = Absence.fromAbsenceRequestDtoToAbsence(dto, student);
        Absence saved = absenceRepository.save(absence);

        studentRepository.incrementAbsences(student.getStudentId());   // même transaction

        return AbsenceResponseDto.fromAbsenceToAbsenceResponseDto(saved);
    }

    @Override
    @Transactional
    public AbsenceResponseDto updateAbsence(AbsenceRequestDto dto, UUID absenceId) {
        Absence absence = findAbsenceOrThrow(absenceId);
        // L'étudiant ne change pas, donc le compteur non plus.
        // Adapte aux champs réels de Absence :
        // absence.setAbsenceDate(dto.getAbsenceDate());
        // absence.setJustified(dto.isJustified());
        // absence.setReason(dto.getReason());
        return AbsenceResponseDto.fromAbsenceToAbsenceResponseDto(absenceRepository.save(absence));
    }

    @Override
    @Transactional(readOnly = true)
    public AbsenceResponseDto getAbsenceById(UUID absenceId) {
        return AbsenceResponseDto.fromAbsenceToAbsenceResponseDto(findAbsenceOrThrow(absenceId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsenceResponseDto> getAbsencesByStudentId(UUID studentId) {
        findStudentOrThrow(studentId);
        return absenceRepository.findByStudentStudentIdOrderByAbsenceDateDesc(studentId).stream()
                .map(AbsenceResponseDto::fromAbsenceToAbsenceResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAbsence(UUID absenceId) {
        Absence absence = findAbsenceOrThrow(absenceId);
        UUID studentId = absence.getStudent().getStudentId();

        absenceRepository.delete(absence);
        studentRepository.decrementAbsences(studentId);   // même transaction
    }

    private Absence findAbsenceOrThrow(UUID absenceId) {
        return absenceRepository.findById(absenceId)
                .orElseThrow(() -> new NoSuchElementException("Absence not found with ID : " + absenceId));
    }

    private Student findStudentOrThrow(UUID studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found with ID : " + studentId));
    }
}
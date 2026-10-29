package org.example.student_management_system.services;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.student_management_system.Dto.StudentFilterDto;
import org.example.student_management_system.Dto.StudentListView;
import org.example.student_management_system.Dto.StudentRequestDto;
import org.example.student_management_system.Dto.StudentResponseDto;
import org.example.student_management_system.entities.Student;
import org.example.student_management_system.enums.StudentStatus; // Import de l'enum
import org.example.student_management_system.Repository.StudentRepository;
import org.example.student_management_system.services.IStudentService;
import org.example.student_management_system.specifications.StudentSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService {

    private final StudentRepository studentRepository;

    @Override
    public StudentResponseDto addStudent(StudentRequestDto studentRequestDto) {
        Student student = Student.fromStudentRequestDtoToStudent(studentRequestDto);
        Student savedStudent = studentRepository.save(student);
        return StudentResponseDto.fromStudentToStudentResponseDto(savedStudent);
    }

    @Override
    public StudentResponseDto updateStudent(StudentRequestDto studentRequestDto, UUID studentId) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found with ID : " + studentId));

        existingStudent.setStudentNumber(studentRequestDto.getStudentNumber());
        existingStudent.setFirstName(studentRequestDto.getFirstName());
        existingStudent.setLastName(studentRequestDto.getLastName());
        existingStudent.setEmail(studentRequestDto.getEmail());
        existingStudent.setBirthDate(studentRequestDto.getBirthDate());
        existingStudent.setMajor(studentRequestDto.getMajor());
        existingStudent.setGender(studentRequestDto.getGender());
        existingStudent.setEnrollmentYear(studentRequestDto.getEnrollmentYear());
        existingStudent.setStudyLevel(studentRequestDto.getStudyLevel());
        existingStudent.setMedicalFilePdf(studentRequestDto.getMedicalFilePdf());
        existingStudent.setStudentStatus(studentRequestDto.getStudentStatus());
        existingStudent.setAbsencesCount(studentRequestDto.getAbsencesCount());

        Student updatedStudent = studentRepository.save(existingStudent);
        return StudentResponseDto.fromStudentToStudentResponseDto(updatedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentListView> search(StudentFilterDto f, Pageable pageable) {
        Specification<Student> spec = Specification
                .where(StudentSpecifications.keyword(f.getKeyword()))
                .and(StudentSpecifications.levelIn(f.getStudyLevels()))
                .and(StudentSpecifications.enrollmentYearBetween(f.getEnrollmentYearFrom(), f.getEnrollmentYearTo()))
                .and(StudentSpecifications.absencesBetween(f.getMinAbsences(), f.getMaxAbsences()));

        return studentRepository.findBy(spec,
                q -> q.as(StudentListView.class).page(pageable));
    }

    @Override
    public void archiveStudent(UUID studentId) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found with ID : " + studentId));

        // On ne supprime pas la ligne de la base, on change juste son statut en DROPPED (Archivé)
        existingStudent.setStudentStatus(StudentStatus.DROPPED);
        studentRepository.save(existingStudent);
    }
    @Override
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentById(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found with ID : " + studentId));
        return StudentResponseDto.fromStudentToStudentResponseDto(student);
    }
}
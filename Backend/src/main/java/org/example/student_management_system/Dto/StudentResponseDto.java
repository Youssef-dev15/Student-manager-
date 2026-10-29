package org.example.student_management_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.student_management_system.entities.Student;
import org.example.student_management_system.enums.StudentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseDto {
    private UUID studentId;
    private String studentNumber;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String major;
    private String gender;
    private Integer enrollmentYear;
    private String studyLevel;
    private String medicalFilePdf;
    private Boolean hasMedicalFile;
    private StudentStatus studentStatus;
    private Integer absencesCount;
    private LocalDateTime createdAt;

    public static StudentResponseDto fromStudentToStudentResponseDto(Student student) {
        return StudentResponseDto.builder()
                .studentId(student.getStudentId())
                .studentNumber(student.getStudentNumber())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .birthDate(student.getBirthDate())
                .major(student.getMajor())
                .gender(student.getGender())
                .enrollmentYear(student.getEnrollmentYear())
                .studyLevel(student.getStudyLevel())
                .medicalFilePdf(student.getMedicalFilePdf())
                .hasMedicalFile(student.getMedicalFilePdf() != null && !student.getMedicalFilePdf().isEmpty())
                .studentStatus(student.getStudentStatus())
                .absencesCount(student.getAbsencesCount())
                .createdAt(student.getCreatedAt())
                .build();
    }
}
package org.example.student_management_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.student_management_system.enums.StudentStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequestDto {
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
    private StudentStatus studentStatus;
    private Integer absencesCount;
}
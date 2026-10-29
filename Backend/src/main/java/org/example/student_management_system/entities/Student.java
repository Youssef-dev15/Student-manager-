package org.example.student_management_system.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.student_management_system.Dto.StudentRequestDto;
import org.example.student_management_system.enums.StudentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "students")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID studentId;

    @Column(unique = true, nullable = false)
    private String studentNumber; // Matricule unique

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDate birthDate;  // Date de naissance

    private String major;         // Filière

    private String gender;        // Genre

    private Integer enrollmentYear;
    private String studyLevel;
    private String medicalFilePdf;

    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus; // ACTIVE, SUSPENDED, GRADUATED, DROPPED

    private Integer absencesCount;

    private LocalDateTime createdAt; // Date de création automatique

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.studentStatus == null) {
            this.studentStatus = StudentStatus.ACTIVE; // Statut par défaut
        }
    }

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GradeReport> gradeReports;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Absence> Absence;

    public static Student fromStudentRequestDtoToStudent(StudentRequestDto studentRequestDto) {
        return Student.builder()
                .studentNumber(studentRequestDto.getStudentNumber())
                .firstName(studentRequestDto.getFirstName())
                .lastName(studentRequestDto.getLastName())
                .email(studentRequestDto.getEmail())
                .birthDate(studentRequestDto.getBirthDate())
                .major(studentRequestDto.getMajor())
                .gender(studentRequestDto.getGender())
                .enrollmentYear(studentRequestDto.getEnrollmentYear())
                .studyLevel(studentRequestDto.getStudyLevel())
                .medicalFilePdf(studentRequestDto.getMedicalFilePdf())
                .studentStatus(studentRequestDto.getStudentStatus())
                .absencesCount(studentRequestDto.getAbsencesCount())
                .build();
    }
}
package org.example.student_management_system.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.student_management_system.Dto.AbsenceRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "absence", indexes = {
        @Index(name = "idx_absence_student_date", columnList = "student_id, absence_date")
})
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID absenceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(nullable = false)
    private LocalDate absenceDate;

    private boolean justified;
    private String recordedBy;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static Absence fromAbsenceRequestDtoToAbsence(AbsenceRequestDto dto, Student student) {
        return Absence.builder()
                .absenceDate(dto.getAbsenceDate())
                .justified(dto.isJustified())
                .recordedBy(dto.getRecordedBy())
                .student(student)
                .build();
    }


}
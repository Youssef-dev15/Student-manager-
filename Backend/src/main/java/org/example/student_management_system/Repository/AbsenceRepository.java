package org.example.student_management_system.Repository;

import org.example.student_management_system.entities.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AbsenceRepository extends JpaRepository<Absence, UUID> {
    List<Absence> findByStudentStudentIdOrderByAbsenceDateDesc(UUID studentId);
}
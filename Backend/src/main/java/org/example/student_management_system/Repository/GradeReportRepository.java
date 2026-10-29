package org.example.student_management_system.Repository;

import org.example.student_management_system.entities.GradeReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GradeReportRepository extends JpaRepository<GradeReport, UUID> {

    List<GradeReport> findByStudentStudentId(UUID studentId);
}
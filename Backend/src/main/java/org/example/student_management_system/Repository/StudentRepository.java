package org.example.student_management_system.Repository;

import org.example.student_management_system.entities.Student;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> , JpaSpecificationExecutor<Student> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Student s SET s.absencesCount = s.absencesCount + 1 WHERE s.studentId = :id")
    int incrementAbsences(@Param("id") UUID id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Student s SET s.absencesCount = s.absencesCount - 1 WHERE s.studentId = :id AND s.absencesCount > 0")
    int decrementAbsences(@Param("id") UUID id);
}
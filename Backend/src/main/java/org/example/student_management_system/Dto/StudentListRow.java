package org.example.student_management_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class StudentListRow implements StudentListView {
    private final UUID studentId;
    private final String firstName;
    private final String lastName;
    private final Integer enrollmentYear;
    private final String studyLevel;
    private final Integer absencesCount;
}
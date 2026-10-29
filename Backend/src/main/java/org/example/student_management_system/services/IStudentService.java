package org.example.student_management_system.services;

import org.example.student_management_system.Dto.StudentFilterDto;
import org.example.student_management_system.Dto.StudentListView;
import org.example.student_management_system.Dto.StudentRequestDto;
import org.example.student_management_system.Dto.StudentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IStudentService {
    public StudentResponseDto addStudent(StudentRequestDto studentRequestDto);
    public StudentResponseDto updateStudent(StudentRequestDto studentRequestDto, UUID studentId);
    Page<StudentListView> search(StudentFilterDto filter, Pageable pageable);
    public StudentResponseDto getStudentById(UUID studentId);
    public void archiveStudent(UUID studentId);
}
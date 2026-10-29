package org.example.student_management_system.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.student_management_system.Dto.StudentFilterDto;
import org.example.student_management_system.Dto.StudentListView;
import org.example.student_management_system.Dto.StudentRequestDto;
import org.example.student_management_system.Dto.StudentResponseDto;
import org.example.student_management_system.services.IStudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final IStudentService studentService;


    @GetMapping("/search")
    public Page<StudentListView> search(
            @Valid @ModelAttribute StudentFilterDto filter,
            @PageableDefault(size = 20, sort = "lastName") Pageable pageable) {
        return studentService.search(filter, pageable);
    }
    @GetMapping("/{id}")
    public StudentResponseDto getById(@PathVariable UUID id) {
        return studentService.getStudentById(id);
    }

    @PostMapping
    public ResponseEntity<StudentResponseDto> create(@Valid @RequestBody StudentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.addStudent(dto));
    }

    @PutMapping("/{id}")
    public StudentResponseDto update(@PathVariable UUID id,
                                     @Valid @RequestBody StudentRequestDto dto) {
        return studentService.updateStudent(dto, id);
    }
    @PatchMapping("/{id}/archive")
    public ResponseEntity<Void> archive(@PathVariable UUID id) {
        studentService.archiveStudent(id);
        return ResponseEntity.noContent().build();
    }
}
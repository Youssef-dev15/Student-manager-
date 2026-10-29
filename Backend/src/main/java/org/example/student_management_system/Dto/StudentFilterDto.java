package org.example.student_management_system.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class StudentFilterDto {

    @Size(max = 50)
    private String keyword;

    @Size(max = 10)
    private List<String> studyLevels;


    @Min(1900) @Max(2100)
    private Integer enrollmentYearFrom;

    @Min(1900) @Max(2100)
    private Integer enrollmentYearTo;


    @Min(0)
    private Integer minAbsences;

    @Min(0)
    private Integer maxAbsences;
}
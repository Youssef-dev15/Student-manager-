package org.example.student_management_system.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class LoginRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
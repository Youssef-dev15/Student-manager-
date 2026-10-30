package org.example.student_management_system.Dto;

public record LoginResponseDto(
        String accessToken,
        String tokenType,
        String email,
        String fullName,
        String role
) {}
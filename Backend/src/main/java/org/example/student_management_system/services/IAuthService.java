package org.example.student_management_system.services;

import org.example.student_management_system.Dto.LoginRequestDto;
import org.example.student_management_system.Dto.LoginResponseDto;
import org.example.student_management_system.Dto.RegisterRequestDto;
import org.example.student_management_system.Dto.UserResponseDto;

public interface IAuthService {
    UserResponseDto register(RegisterRequestDto dto);
    LoginResponseDto login(LoginRequestDto dto);
}
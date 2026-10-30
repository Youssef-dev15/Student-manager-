package org.example.student_management_system.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.student_management_system.Dto.LoginRequestDto;
import org.example.student_management_system.Dto.LoginResponseDto;
import org.example.student_management_system.Dto.RegisterRequestDto;
import org.example.student_management_system.Dto.UserResponseDto;
import org.example.student_management_system.services.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }
    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto dto) {
        return authService.login(dto);
    }
}
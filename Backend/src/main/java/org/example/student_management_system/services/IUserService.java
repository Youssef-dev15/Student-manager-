package org.example.student_management_system.services;

import org.example.student_management_system.Dto.UserRequestDto;
import org.example.student_management_system.Dto.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    UserResponseDto addUser(UserRequestDto userRequestDto);
    UserResponseDto updateUser(UserRequestDto userRequestDto, UUID userId);
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(UUID userId);
    UserResponseDto validateManagerAccount(UUID userId);
    UserResponseDto blockManagerAccount(UUID userId);
    UserResponseDto unblockManagerAccount(UUID userId);
    void deleteUser(UUID userId);
}
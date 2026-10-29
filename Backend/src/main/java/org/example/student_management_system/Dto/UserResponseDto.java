package org.example.student_management_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.student_management_system.Dto.UserRequestDto;
import org.example.student_management_system.entities.User;
import org.example.student_management_system.enums.Role;
import org.example.student_management_system.enums.Status;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private UUID userId;
    private String email;
    private String fullName;
    private Role role;
    private Status status;

    public static UserResponseDto fromUserToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

}
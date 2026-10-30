package org.example.student_management_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.student_management_system.enums.Role;
import org.example.student_management_system.enums.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {
    private String email;
    private String password;
    private String fullName;
    private Role role;
    private Status status;
}
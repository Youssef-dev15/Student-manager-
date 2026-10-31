package org.example.student_management_system.services;

import lombok.RequiredArgsConstructor;
import org.example.student_management_system.Dto.RegisterRequestDto;
import org.example.student_management_system.Dto.UserResponseDto;
import org.example.student_management_system.Repository.UserRepository;
import org.example.student_management_system.entities.User;
import org.example.student_management_system.enums.Role;
import org.example.student_management_system.enums.Status;
import org.example.student_management_system.services.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.example.student_management_system.Dto.LoginRequestDto;
import org.example.student_management_system.Dto.LoginResponseDto;
import org.example.student_management_system.security.JwtService;
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserResponseDto register(RegisterRequestDto dto) {
        String email = dto.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet email est déjà utilisé");
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .role(Role.Manager)
                .status(Status.Pending)
                .build();

        return UserResponseDto.fromUserToUserResponseDto(userRepository.save(user));
    }
    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto dto) {
        String email = dto.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(dto.getPassword(), u.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "incorrect credentials"));

        if (user.getStatus() == Status.Pending) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Account awaiting validation by an administrator");
        }
        if (user.getStatus() == Status.Blocked) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, " blocked Account");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponseDto(token, "Bearer", user.getEmail(),
                user.getFullName(), user.getRole().name());
    }
}
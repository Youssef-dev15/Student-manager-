package org.example.student_management_system.services;

import lombok.RequiredArgsConstructor;
import org.example.student_management_system.Dto.UserRequestDto;
import org.example.student_management_system.Dto.UserResponseDto;
import org.example.student_management_system.Repository.UserRepository;
import org.example.student_management_system.entities.User;
import org.example.student_management_system.enums.Role;
import org.example.student_management_system.enums.Status;
import org.example.student_management_system.services.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IAuditLogService auditLogService;

    @Override
    @Transactional
    public UserResponseDto addUser(UserRequestDto dto) {
        User user = User.fromUserRequestDtoToUser(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return UserResponseDto.fromUserToUserResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UserRequestDto dto, UUID userId) {
        User user = findUserOrThrow(userId);
        // On ne modifie ni le rôle ni le statut ici : ils ont leurs méthodes dédiées
        // (adapte les setters à tes champs réels)
        // user.setUsername(dto.getUsername());
        // user.setEmail(dto.getEmail());
        return UserResponseDto.fromUserToUserResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::fromUserToUserResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(UUID userId) {
        return UserResponseDto.fromUserToUserResponseDto(findUserOrThrow(userId));
    }

    @Override
    @Transactional
    public UserResponseDto validateManagerAccount(UUID userId) {
        User user = findManagerOrThrow(userId);
        if (user.getStatus() != Status.Pending) {
            throw new IllegalStateException("Only a pending account can be approved");
        }
        user.setStatus(Status.Approved);
        auditLogService.record("ACCOUNT_APPROVED", "Approved account " + user.getEmail());
        return UserResponseDto.fromUserToUserResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto blockManagerAccount(UUID userId) {
        User user = findManagerOrThrow(userId);   // refuse si c'est un ADMIN
        if (user.getStatus() != Status.Approved) {
            throw new IllegalStateException("Only an approved account can be blocked");
        }
        user.setStatus(Status.Blocked);
        auditLogService.record("ACCOUNT_BLOCKED", "Blocked account " + user.getEmail());
        return UserResponseDto.fromUserToUserResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto unblockManagerAccount(UUID userId) {
        User user = findManagerOrThrow(userId);
        if (user.getStatus() != Status.Blocked) {
            throw new IllegalStateException("Only a blocked account can be unblocked");
        }
        user.setStatus(Status.Approved);
        auditLogService.record("ACCOUNT_UNBLOCKED", "Unblocked account " + user.getEmail());
        return UserResponseDto.fromUserToUserResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        User user = findUserOrThrow(userId);
        if (user.getRole() == Role. Admin) {
            throw new IllegalStateException("An Admin account cannot be deleted");
        }
        userRepository.delete(user);
        auditLogService.record("ACCOUNT_DELETED", "Deleted account " + user.getEmail());
    }

    private User findUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID : " + userId));
    }

    private User findManagerOrThrow(UUID userId) {
        User user = findUserOrThrow(userId);
        if (user.getRole() != Role.Manager) {
            throw new IllegalStateException("This action only applies to Manager accounts");
        }
        return user;
    }
}
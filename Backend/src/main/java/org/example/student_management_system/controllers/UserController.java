package org.example.student_management_system.controllers;

import lombok.RequiredArgsConstructor;
import org.example.student_management_system.Dto.UserResponseDto;
import org.example.student_management_system.services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping
    public List<UserResponseDto> getAll() { return userService.getAllUsers(); }

    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable UUID id) { return userService.getUserById(id); }

    @PatchMapping("/{id}/validate")
    public UserResponseDto validate(@PathVariable UUID id) { return userService.validateManagerAccount(id); }

    @PatchMapping("/{id}/block")
    public UserResponseDto block(@PathVariable UUID id) { return userService.blockManagerAccount(id); }

    @PatchMapping("/{id}/unblock")
    public UserResponseDto unblock(@PathVariable UUID id) { return userService.unblockManagerAccount(id); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
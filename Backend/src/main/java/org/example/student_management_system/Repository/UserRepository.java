package org.example.student_management_system.Repository;

import org.example.student_management_system.entities.User;
import org.example.student_management_system.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    boolean existsByRole(Role role);
}

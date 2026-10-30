package org.example.student_management_system.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.student_management_system.Repository.UserRepository;
import org.example.student_management_system.entities.User;
import org.example.student_management_system.enums.Role;
import org.example.student_management_system.enums.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.existsByRole(Role.Admin)) {
            return;
        }
        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            log.warn("Aucun Admin n'existe et ADMIN_EMAIL / ADMIN_PASSWORD ne sont pas définies : "
                    + "aucun compte Admin n'a été créé.");
            return;
        }

        User admin = User.builder()
                .email(adminEmail.trim().toLowerCase())
                .password(passwordEncoder.encode(adminPassword))
                .fullName("Administrateur")
                .role(Role.Admin)
                .status(Status.Approved)
                .build();

        userRepository.save(admin);
        log.info("Compte Admin créé : {}", admin.getEmail());
    }
}
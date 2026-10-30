package org.example.student_management_system.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.student_management_system.Repository.UserRepository;
import org.example.student_management_system.enums.Status;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtService.isTokenValid(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                String email = jwtService.extractEmail(token);

                userRepository.findByEmail(email)
                        .filter(user -> user.getStatus() == Status.Approved)
                        .ifPresent(user -> {
                            var authority = new SimpleGrantedAuthority(
                                    "ROLE_" + user.getRole().name().toUpperCase());
                            var auth = new UsernamePasswordAuthenticationToken(
                                    user.getEmail(), null, List.of(authority));
                            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        });
            }
        }

        filterChain.doFilter(request, response);
    }
}
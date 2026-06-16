package org.example.roomreservation.service;

import lombok.RequiredArgsConstructor;
import org.example.roomreservation.model.dto.AuthResponseDTO;
import org.example.roomreservation.model.dto.RegisterRequestDTO;
import org.example.roomreservation.model.entity.Role;
import org.example.roomreservation.model.entity.User;
import org.example.roomreservation.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequestDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email-ul este deja înregistrat");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .floor(dto.getFloor())
                .department(dto.getDepartment())
                .enabled(true)
                .build();

        userRepository.save(user);

    }

    public List<AuthResponseDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> AuthResponseDTO.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .build())
                .toList();
    }
}
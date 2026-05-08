package org.example.roomreservation.service;

import lombok.RequiredArgsConstructor;
import org.example.roomreservation.model.dto.AuthRequestDTO;
import org.example.roomreservation.model.dto.AuthResponseDTO;
import org.example.roomreservation.model.dto.RegisterRequestDTO;
import org.example.roomreservation.model.entity.Role;
import org.example.roomreservation.model.entity.User;
import org.example.roomreservation.repository.UserRepository;
import org.example.roomreservation.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO dto){

        if(userRepository.existsByEmail(dto.getEmail())){
            throw new IllegalArgumentException("Email already exists");
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

        String token = jwtUtil.generateToken(user);

        return AuthResponseDTO.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();

    }

    public AuthResponseDTO login(AuthRequestDTO dto){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow();

        String token = jwtUtil.generateToken(user);

        return AuthResponseDTO.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public List<AuthResponseDTO> getUsers(){
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> AuthResponseDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build())
                .toList();
    }
}

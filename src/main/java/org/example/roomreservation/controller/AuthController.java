package org.example.roomreservation.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.roomreservation.model.dto.AuthRequestDTO;
import org.example.roomreservation.model.dto.AuthResponseDTO;
import org.example.roomreservation.model.dto.RegisterRequestDTO;
import org.example.roomreservation.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponseDTO register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        System.out.println("REGISTER HIT");
        return authService.register(registerRequestDTO);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        System.out.println("LOGIN HIT");
        return authService.login(authRequestDTO);
    }

    @GetMapping("/users")
    public List<AuthResponseDTO> getUsers() {
        return authService.getUsers();
    }
}

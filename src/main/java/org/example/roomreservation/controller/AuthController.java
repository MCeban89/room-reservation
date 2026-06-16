package org.example.roomreservation.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.roomreservation.model.dto.RegisterRequestDTO;
import org.example.roomreservation.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login"; // caută templates/auth/login.html
    }


    @GetMapping("/register")
    public String registerPage(Model model) {
        // Trimitem un obiect gol pentru th:object în formular
        model.addAttribute("registerForm", new RegisterRequestDTO());
        return "auth/register"; // caută templates/auth/register.html
    }

    // ================================================================
    // POST /auth/register — procesează înregistrarea
    // ================================================================
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerForm") RegisterRequestDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Dacă există erori de validare (@NotBlank, @Email etc.)
        // returnăm pagina cu erorile afișate
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            authService.register(dto);
            // După înregistrare redirecționăm la login cu mesaj de succes
            redirectAttributes.addFlashAttribute("success",
                    "Cont creat cu succes! Te poți autentifica acum.");
            return "redirect:/auth/login";

        } catch (IllegalArgumentException e) {
            // Email deja există
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
}
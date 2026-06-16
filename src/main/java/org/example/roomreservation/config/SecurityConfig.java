package org.example.roomreservation.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    // NU mai avem JwtAuthFilter — Thymeleaf folosește sesiuni, nu JWT

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF rămâne ACTIV pentru Thymeleaf — formularele HTML au nevoie de el
                // Thymeleaf adaugă automat token-ul CSRF în formulare cu th:action
                // Nu mai dezactivăm ca la REST

                // Resurse statice — CSS, JS, imagini
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()

                        // Pagini publice
                        .requestMatchers("/auth/login", "/auth/register").permitAll()

                        // Swagger — pentru prezentare la comisie
                        .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll()

                        // Doar ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Orice altceva necesită autentificare
                        .anyRequest().authenticated()
                )

                // Form login — Spring Security gestionează autentificarea automat
                // Când userul trimite formularul, Spring verifică email + parolă
                .formLogin(form -> form
                        .loginPage("/auth/login")           // pagina noastră de login
                        .loginProcessingUrl("/auth/login")  // URL-ul unde se trimite formularul
                        .defaultSuccessUrl("/rooms", true) // după login merge la /rooms
                        .failureUrl("/auth/login?error")    // după eșec merge înapoi cu ?error
                        .permitAll()
                )

                // Logout
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .permitAll()
                )

                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
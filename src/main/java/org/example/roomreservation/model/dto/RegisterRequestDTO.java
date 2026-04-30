package org.example.roomreservation.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Numele este obligatoriu")
    private String name;

    @NotBlank
    @Email(message = "Email invalid")
    private String email;

    @NotBlank
    @Size(min = 6, message = "Password min 6 ch")
    private String password;

    private Integer floor;
    private String department;
}

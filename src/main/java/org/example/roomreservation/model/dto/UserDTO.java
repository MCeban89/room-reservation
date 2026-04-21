package org.example.roomreservation.model.dto;

import org.example.roomreservation.model.entity.Role;

import java.time.LocalDateTime;

public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Role role;
    private Integer floor;
    private String department;
    private Boolean enabled;
    private LocalDateTime createdAt;
}

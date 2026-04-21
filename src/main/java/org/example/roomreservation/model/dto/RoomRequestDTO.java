package org.example.roomreservation.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequestDTO {
    @NotBlank
    private String roomName;

    @NotNull
    @Min(value = 1)
    private Integer capacity;

    @NotNull
    private Integer floor;

    private String description;

    private Boolean hasProjector = false;
    private Boolean hasWhiteboard = false;
    private Boolean hasVideoConference = false;

}

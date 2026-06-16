package org.example.roomreservation.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.roomreservation.model.entity.ReservationStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponseDTO {
    private Long id;

    //room
    private Long roomId;
    private String roomName;
    private Integer roomFloor;
    //user
    private Long userId;
    private String userName;
    private String userEmail;
    //reservation
    // În interiorul clasei ReservationResponseDTO.java
    @JsonProperty("start")
    private LocalDateTime startTime;
    @JsonProperty("end")
    private LocalDateTime endTime;
    private Integer participants;
    private String title;
    private String description;
    private ReservationStatus status;

    private LocalDateTime createdAt;

}

package org.example.roomreservation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDTO {
    private Long roomId;
    private String roomName;
    private Integer capacity;
    private Integer floor;
    private String description;
    private Boolean hasProjector;
    private Boolean hasWhiteboard;
    private Boolean VideoConference;
    private Boolean active;
}

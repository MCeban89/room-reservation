package org.example.roomreservation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendationDTO {

    private Long roomId;
    private String roomName;
    private Integer capacity;
    private Integer floor;
    private Boolean hasWhiteboard;
    private Boolean hasVideoConference;
    private Boolean hasProjector;
    private Integer score;
    private List<String> reasons;

}

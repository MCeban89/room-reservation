package org.example.roomreservation.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestDTO {

    @NotNull(message = "ID-ul sălii este obligatoriu")
    private Long roomId;

    @NotNull
    @Future(message = "Data de start trebuie să fie în viitor")
    @JsonProperty("start")
    private LocalDateTime startTime;

    @NotNull
    @JsonProperty("end")
    private LocalDateTime endTime;


    @NotNull
    @Min(value = 1, message = "Trebuie cel puțin 1 participant")
    private Integer participants;

    private String title;   // ex: "Daily standup"
    private String notes;   // observații opționale
}

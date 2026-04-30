package org.example.roomreservation.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.roomreservation.model.dto.ReservationRequestDTO;
import org.example.roomreservation.model.dto.ReservationResponseDTO;
import org.example.roomreservation.model.entity.Reservation;
import org.example.roomreservation.model.entity.Room;
import org.example.roomreservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController {
    private ReservationService reservationService;

//    @PutMapping
//    public ResponseEntity<ReservationResponseDTO> create(@Valid @RequestBody ReservationRequestDTO reservationRequestDTO) {
//        //ReservationResponseDTO result = reservationService.create(dto,User);
//    }

    @GetMapping("/all")
    public List<Reservation> allReservations()
    {
        return reservationService.allReservations();
    }


}

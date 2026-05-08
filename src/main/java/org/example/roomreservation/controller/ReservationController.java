package org.example.roomreservation.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.roomreservation.model.dto.ReservationRequestDTO;
import org.example.roomreservation.model.dto.ReservationResponseDTO;
import org.example.roomreservation.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> create(@Valid @RequestBody ReservationRequestDTO reservationRequestDTO) {
        ReservationResponseDTO result = reservationService.createReservation(reservationRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result); // ← 201
    }

    @GetMapping("/all")
    public List<ReservationResponseDTO> getAllReservations() {
        return reservationService.allReservations();
    }

    @GetMapping("/my")
    public List<ReservationResponseDTO>  getMyReservations()
    {
        return reservationService.getMyReservations();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id){
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }


}

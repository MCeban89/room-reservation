package org.example.roomreservation.controller;

import lombok.AllArgsConstructor;
import org.example.roomreservation.model.dto.ReservationRequestDTO;
import org.example.roomreservation.model.dto.ReservationResponseDTO;
import org.example.roomreservation.repository.ReservationRepository;
import org.example.roomreservation.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@AllArgsConstructor
public class EventRestController {

    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    @GetMapping
    public List<ReservationResponseDTO> getEvents(
            @RequestParam("roomId") Long roomId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {


        return reservationRepository.findConfirmedOrAttendedByRoomId(roomId).stream()
                .map(reservation -> ReservationResponseDTO.builder()
                        .id(reservation.getId())
                        .title(reservation.getTitle())
                        .startTime(reservation.getStartTime())
                        .endTime(reservation.getEndTime())
                        .roomId(reservation.getRoom().getId())
                        .build())
                .toList();
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveEvent(@RequestBody ReservationRequestDTO dto) {
        reservationService.createReservation(dto);

        return ResponseEntity.ok("Salvat cu succes!");
    }
}
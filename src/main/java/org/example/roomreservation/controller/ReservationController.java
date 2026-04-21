package org.example.roomreservation.controller;

import lombok.AllArgsConstructor;
import org.example.roomreservation.model.entity.Reservation;
import org.example.roomreservation.model.entity.Room;
import org.example.roomreservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController {
    private ReservationService reservationService;

    @PutMapping
    public void createReservation(@RequestBody Reservation room)
    {

    }

    @GetMapping("/all")
    public List<Reservation> allReservations()
    {
        return reservationService.allReservations();
    }


}

package org.example.roomreservation.service;

import lombok.AllArgsConstructor;
import org.example.roomreservation.model.entity.Reservation;
import org.example.roomreservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {
    private ReservationRepository reservationRepository;

    public List<Reservation> allReservations()
    {
        return reservationRepository.findAll();
    }
}

package org.example.roomreservation.service;

import org.example.roomreservation.model.entity.ReservationStatus;
import org.example.roomreservation.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationScheduler {


    private final ReservationRepository reservationRepository;

    public ReservationScheduler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void checkAndCloseExpiredReservations(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationThreshold = LocalDateTime.now().minusMinutes(15);

        reservationRepository.updatedExpiredReservations(now,
                ReservationStatus.ATTENDED,
                ReservationStatus.CANCELLED);

        reservationRepository.cancelExpiredReservations(expirationThreshold,
                ReservationStatus.CONFIRMED,
                ReservationStatus.CANCELLED);

    }


}

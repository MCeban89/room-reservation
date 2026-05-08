package org.example.roomreservation.service;

import lombok.AllArgsConstructor;
import org.example.roomreservation.model.dto.ReservationMapper;
import org.example.roomreservation.model.dto.ReservationRequestDTO;
import org.example.roomreservation.model.dto.ReservationResponseDTO;
import org.example.roomreservation.model.entity.*;
import org.example.roomreservation.repository.ReservationHistoryRepository;
import org.example.roomreservation.repository.ReservationRepository;
import org.example.roomreservation.repository.RoomRepository;
import org.example.roomreservation.repository.UserRepository;
import org.example.roomreservation.security.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {
    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;
    private SecurityUtils securityUtils;
    private ReservationHistoryRepository historyRepository;

    public List<ReservationResponseDTO> allReservations()
    {
        return reservationRepository.findAll().stream().map(ReservationMapper::toDto).toList();
    }

    public List<ReservationResponseDTO> getMyReservations()
    {
        User user = securityUtils.getCurrentUser();

        List<Reservation> reservations = reservationRepository.findByUser(user);

        return reservations.stream().map(ReservationMapper::toDto).toList();
    }

    public ReservationResponseDTO createReservation(ReservationRequestDTO dto) {

        boolean conflict = reservationRepository.existsConflict(
                dto.getRoomId(), dto.getStartTime(), dto.getEndTime()
        );
        if (conflict) {
            throw new RuntimeException("Sala nu este disponibila in acest interval");
        }

        Room room = roomRepository.findById(dto.getRoomId()).orElseThrow(() -> new RuntimeException("Room Not Found"));

        User user = securityUtils.getCurrentUser();

        Reservation reservation = Reservation.builder()
                .room(room)
                .user(user)
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .participants(dto.getParticipants())
                .status(ReservationStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .build();

        reservationRepository.save(reservation);

        historyRepository.save(ReservationHistory.builder()
                .user(user)
                .room(room)
                .reservationDate(dto.getStartTime())
                .build());

        return  ReservationMapper.toDto(reservation);
    }


    public void deleteReservation(Long id) {
        String email = securityUtils.getCurrentUserEmail();

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        boolean isOwner = reservation.getUser().getEmail().equals(email);
        boolean isAdmin = securityUtils.isCurrentUserAdmin();

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("You cannot delete this reservation");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }
}

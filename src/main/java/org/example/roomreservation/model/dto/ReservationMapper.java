package org.example.roomreservation.model.dto;

import org.example.roomreservation.model.entity.Reservation;

public class ReservationMapper {

    public static ReservationResponseDTO toDto(Reservation reservation) {

        return ReservationResponseDTO.builder()
                .id(reservation.getId())
                .createdAt(reservation.getCreatedAt())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .participants(reservation.getParticipants())
                .roomId(reservation.getRoom().getId())
                .roomFloor(reservation.getRoom().getFloor())
                .roomName(reservation.getRoom().getName())
                .userId(reservation.getUser().getId())
                .userEmail(reservation.getUser().getEmail())
                .userName(reservation.getUser().getName())
                .status(reservation.getStatus())
                .title(reservation.getTitle())
                .build();
    }
}

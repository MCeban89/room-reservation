package org.example.roomreservation.repository;

import org.example.roomreservation.model.entity.ReservationHistory;
import org.example.roomreservation.model.entity.Room;
import org.example.roomreservation.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {
    @Query("SELECT COUNT(h) FROM ReservationHistory h WHERE " +
            "h.user = :user " +
            "AND h.room = :room " +
            "AND h.reservationDate >= :afterDate")
    long countByUserAndRoomAndDateAfter(
            @Param("user") User user,
            @Param("room") Room room,
            @Param("afterDate") LocalDateTime afterDate
    );
}

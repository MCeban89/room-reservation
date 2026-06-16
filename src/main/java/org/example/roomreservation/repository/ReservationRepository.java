package org.example.roomreservation.repository;

import jakarta.transaction.Transactional;
import org.example.roomreservation.model.dto.ReservationResponseDTO;
import org.example.roomreservation.model.entity.Reservation;
import org.example.roomreservation.model.entity.ReservationStatus;
import org.example.roomreservation.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE " +
            "r.room.id = :roomId " +
            "AND r.status IN ('CONFIRMED', 'ATTENDED') " +
            "AND r.startTime < :endTime " +
            "AND r.endTime > :startTime")
    boolean existsConflict(
            @Param("roomId") Long roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    List<Reservation> findByUser(User user);


    //pentru rezervarile care au fost complete
    @Transactional
    @Modifying
    @Query("UPDATE Reservation r SET r.status = :newStatus " +
           "WHERE r.endTime < :now AND r.status = :activeStatus" )
    void updatedExpiredReservations(
            @Param("now") LocalDateTime now,
            @Param("activeStatus")ReservationStatus activeStatus,
            @Param("newStatus") ReservationStatus newStatus
    );


    //pentru  rezervarile la care nu a fost checkin
    @Transactional
    @Modifying
    @Query("UPDATE Reservation r SET r.status = :newStatus " +
            "WHERE r.startTime < :expirationThreshold AND r.status = :activeStatus" )
    void cancelExpiredReservations(
            @Param("expirationThreshold") LocalDateTime expirationThreshold,
            @Param("activeStatus")ReservationStatus activeStatus,
            @Param("newStatus") ReservationStatus newStatus
    );

    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId AND (r.status = 'CONFIRMED' OR r.status = 'ATTENDED')")
    List<Reservation> findConfirmedOrAttendedByRoomId(@Param("roomId") Long roomId);
}

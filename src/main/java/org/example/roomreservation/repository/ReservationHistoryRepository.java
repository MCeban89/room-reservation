package org.example.roomreservation.repository;

import org.example.roomreservation.model.entity.ReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {
}

package org.example.roomreservation.service;

import lombok.RequiredArgsConstructor;
import org.example.roomreservation.model.dto.RecommendationDTO;
import org.example.roomreservation.model.entity.Room;
import org.example.roomreservation.model.entity.User;
import org.example.roomreservation.repository.ReservationHistoryRepository;
import org.example.roomreservation.repository.RoomRepository;
import org.example.roomreservation.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RoomRepository roomRepository;
    private final ReservationHistoryRepository historyRepository;
    private final SecurityUtils securityUtils;

    // ================================================================
    // METODA PRINCIPALĂ
    // Intrare: parametrii cererii (interval, participanți, echipamente)
    // Ieșire: top 3 săli recomandate cu scor și motive
    // ================================================================
    public List<RecommendationDTO> getRecommendations(
            LocalDateTime start,
            LocalDateTime end,
            Integer participants,
            Boolean needsProjector,
            Boolean needsWhiteboard,
            Boolean needsVideoConference) {

        // Pasul 1: Obține userul curent — avem nevoie de etajul lui
        User currentUser = securityUtils.getCurrentUser();

        // Pasul 2: Obține sălile disponibile în intervalul cerut
        // cu capacitate minimă egală cu numărul de participanți
        List<Room> availableRooms = roomRepository.findAvailableRooms(
                start, end, participants);
        // Pasul 3: Pentru fiecare sală disponibilă calculează scorul
        List<RecommendationDTO> recommendations = new ArrayList<>();
        System.out.println(availableRooms.size());
        for (Room room : availableRooms) {
            RecommendationDTO dto = calculateScore(
                    room,
                    currentUser,
                    participants,
                    needsProjector,
                    needsWhiteboard,
                    needsVideoConference,
                    start
            );
            recommendations.add(dto);

        }

        // Pasul 4: Sortează descrescător după scor și returnează top 3
        return recommendations.stream()
                .sorted(Comparator.comparingInt(RecommendationDTO::getScore).reversed())
                .limit(3)
                .toList();
    }


    // ================================================================
    // ALGORITMUL DE SCORING
    // Calculează scorul unei săli pe baza a 4 criterii
    // Scor maxim posibil: 100 puncte
    // ================================================================
    private RecommendationDTO calculateScore(
            Room room,
            User currentUser,
            Integer participants,
            Boolean needsProjector,
            Boolean needsWhiteboard,
            Boolean needsVideoConference,
            LocalDateTime start) {

        int score = 0;
        List<String> reasons = new ArrayList<>();


        // ============================================================
        // CRITERIU 1 — Capacitate potrivită (max 30 puncte)
        //
        // Logică: O sală prea mare e risipă de resurse.
        // Ideal: capacitate între participants și participants * 1.5
        // ============================================================
        double ratio = (double) room.getCapacity() / participants;

        if (ratio >= 1.0 && ratio <= 1.5) {
            // Capacitate optimă — aproape perfect
            score += 30;
            reasons.add("Capacitate optimă (" + room.getCapacity()
                    + " locuri pentru " + participants + " participanți)");

        } else if (ratio > 1.5 && ratio <= 2.0) {
            // Puțin mai mare — acceptabil
            score += 15;
            reasons.add("Capacitate acceptabilă (" + room.getCapacity() + " locuri)");

        } else if (ratio > 2.0) {
            // Sală prea mare — penalizare
            score += 5;
            reasons.add("Sală mai mare decât necesar (" + room.getCapacity() + " locuri)");
        }
        // ratio < 1.0 nu poate apărea — findAvailableRooms filtrează după minCapacity


        // ============================================================
        // CRITERIU 2 — Echipamente cerute (max 30 puncte)
        //
        // Logică: Dacă userul a cerut echipament și sala îl are → bonus
        // Dacă sala are echipament dar userul nu l-a cerut → neutru
        // ============================================================
        if (Boolean.TRUE.equals(needsProjector)) {
            if (Boolean.TRUE.equals(room.getHasProjector())) {
                score += 15;
                reasons.add("Are proiector");
            } else {
                // Userul a cerut proiector dar sala nu are — penalizare mare
                score -= 20;
                reasons.add("Nu are proiector (cerut)");
            }
        }

        if (Boolean.TRUE.equals(needsWhiteboard)) {
            if (Boolean.TRUE.equals(room.getHasWhiteboard())) {
                score += 8;
                reasons.add("Are whiteboard");
            } else {
                score -= 10;
                reasons.add("Nu are whiteboard (cerut)");
            }
        }

        if (Boolean.TRUE.equals(needsVideoConference)) {
            if (Boolean.TRUE.equals(room.getHasVideoConference())) {
                score += 7;
                reasons.add("Are sistem videoconferință");
            } else {
                score -= 10;
                reasons.add("Nu are videoconferință (cerută)");
            }
        }


        // ============================================================
        // CRITERIU 3 — Proximitate etaj (max 20 puncte)
        //
        // Logică: Angajatul preferă să nu meargă prea departe.
        // Același etaj = maxim, diferență mare = 0
        // ============================================================
        if (currentUser.getFloor() != null && room.getFloor() != null) {
            int floorDiff = Math.abs(room.getFloor() - currentUser.getFloor());

            if (floorDiff == 0) {
                score += 20;
                reasons.add("Același etaj cu tine (etaj " + room.getFloor() + ")");
            } else if (floorDiff == 1) {
                score += 10;
                reasons.add("Etaj apropiat (etaj " + room.getFloor() + ")");
            } else if (floorDiff == 2) {
                score += 5;
                reasons.add("Etaj " + room.getFloor());
            }
            // diferență > 2 etaje → 0 puncte, niciun motiv adăugat
        }


        // ============================================================
        // CRITERIU 4 — Istoricul utilizatorului (max 20 puncte)
        //
        // Logică: Dacă userul a mai folosit această sală recent,
        // probabil știe unde e și îi place — o recomandăm mai sus.
        // Perioada analizată: ultimele 30 de zile
        // ============================================================
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        long usageCount = historyRepository.countByUserAndRoomAndDateAfter(
                currentUser,
                room,
                thirtyDaysAgo
        );

        if (usageCount > 0) {
            // Maxim 20 puncte — 5 puncte per utilizare, maxim 4 utilizări
            int historyScore = (int) Math.min(usageCount * 5, 20);
            score += historyScore;

            if (usageCount == 1) {
                reasons.add("Ai mai folosit această sală recent");
            } else {
                reasons.add("Ai folosit această sală de " + usageCount
                        + " ori în ultimele 30 de zile");
            }
        }


        // ============================================================
        // Construiește și returnează DTO-ul cu rezultatul
        // ============================================================
        return RecommendationDTO.builder()
                .roomId(room.getId())
                .roomName(room.getName())
                .floor(room.getFloor())
                .capacity(room.getCapacity())
                .hasProjector(room.getHasProjector())
                .hasWhiteboard(room.getHasWhiteboard())
                .hasVideoConference(room.getHasVideoConference())
                .score(Math.max(score, 0)) // scorul nu poate fi negativ
                .reasons(reasons)
                .build();
    }
}
package org.example.roomreservation.service;

import lombok.AllArgsConstructor;
import org.example.roomreservation.model.dto.ReservationMapper;
import org.example.roomreservation.model.dto.ReservationRequestDTO;
import org.example.roomreservation.model.dto.ReservationResponseDTO;
import org.example.roomreservation.model.entity.*;
import org.example.roomreservation.repository.ReservationHistoryRepository;
import org.example.roomreservation.repository.ReservationRepository;
import org.example.roomreservation.repository.RoomRepository;
import org.example.roomreservation.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {

    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;
    private SecurityUtils securityUtils;
    private ReservationHistoryRepository historyRepository;
    private EmailService emailService;
    private CalendarService calendarService;
    private QrCodeService qrCodeService;

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

    public void createReservation(ReservationRequestDTO dto) {

        // 1. Validare: Timpul de început să nu fie în trecut
        if (dto.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Nu poți face o rezervare în trecut!");
        }

        // 2. Validare NOUĂ: Timpul de final să fie DUPĂ timpul de început
        if (!dto.getEndTime().isAfter(dto.getStartTime())) {
            throw new IllegalArgumentException("Ora de sfârșit trebuie să fie după ora de început!");
        }

        // 3. Extragerea sălii (O singură dată!)
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Sala nu a fost găsită!"));

        // 4. Validare NOUĂ: Verificare capacitate sală
        if (dto.getParticipants() > room.getCapacity()) { // Presupunând că ai `getCapacity()` în entitatea Room
            throw new IllegalArgumentException("Numărul de participanți (" + dto.getParticipants() +
                    ") depășește capacitatea maximă a sălii (" + room.getCapacity() + " locuri)!");
        }

        // 5. Verificare conflicte de timp în baza de date
        boolean conflict = reservationRepository.existsConflict(
                dto.getRoomId(), dto.getStartTime(), dto.getEndTime()
        );
        if (conflict) {
            throw new RuntimeException("Sala nu este disponibilă în acest interval orar!");
        }

        // 6. Preluare utilizator curent
        User user = securityUtils.getCurrentUser();

        // 7. Construirea rezervării (Folosind obiectul 'room' deja extras)
        Reservation reservation = Reservation.builder()
                .room(room)
                .title(dto.getTitle())
                .user(user)
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .participants(dto.getParticipants())
                .status(ReservationStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .build();

        reservationRepository.save(reservation);

        // 8. Generare QR Code și eveniment Calendar
        byte[] qrCode = qrCodeService.generateQrCode("http://localhost:8080/reservations/check-in/" + reservation.getId());

        try {
            byte[] icsBytes = calendarService.generateCalendarEvent(
                    "Rezervare " + room.getName(),
                    "Descriere rezervare",
                    reservation.getStartTime(),
                    reservation.getEndTime()
            );

            // Notă: Ar fi mai bine ca emailul să fie trimis la user.getEmail() în loc de o adresă hardcodată!
            emailService.sendEmail(
                    user.getEmail(), // Schimbat din "ceban.mihail192@gmail.com" pentru a fi dinamic
                    "Confirmare Rezervare: " + room.getName(),
                    "Ați primit emailul de confirmare a rezervării sălii și QR-codul atașat.",
                    icsBytes, qrCode
            );
        } catch (Exception e) {
            // În producție e mai bine să folosești un Logger (ex: log.error("...", e)) decât System.out
            System.err.println("Eroare la trimiterea email-ului: " + e.getMessage());
        }

        // 9. Salvare istoric
        historyRepository.save(ReservationHistory.builder()
                .user(user)
                .room(room)
                .reservationDate(dto.getStartTime())
                .build());
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


    public void checkIn(Long id) {
        Reservation r = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));

        if(r.getStatus() == ReservationStatus.CANCELLED) {
            throw new RuntimeException("Reservation cancelled");
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(r.getStartTime().minusMinutes(10))) {
            throw new RuntimeException("Too early! Check-in opens 10 minutes before the meeting.");
        }

        if (now.isAfter(r.getStartTime().plusMinutes(15))) {
            throw new RuntimeException("Too late! The 15-minute check-in window has expired.");
        }

        r.setStatus(ReservationStatus.ATTENDED);
        ReservationStatus status = r.getStatus();
        reservationRepository.save(r);
    }

}

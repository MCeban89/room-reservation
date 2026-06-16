package org.example.roomreservation.controller;

import lombok.AllArgsConstructor;
import org.example.roomreservation.model.dto.ReservationRequestDTO;
import org.example.roomreservation.repository.ReservationRepository;
import org.example.roomreservation.service.ReservationService;
import org.example.roomreservation.service.RoomService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final RoomService roomService;

    // ================================================================
    // GET /reservations/my
    // ================================================================
    @GetMapping("/my")
    public String myReservations(Model model) {
        model.addAttribute("reservations", reservationService.getMyReservations());
        return "reservations/my";
    }

    // ================================================================
    // GET /reservations/all — doar ADMIN
    // ================================================================
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public String allReservations(Model model) {
        model.addAttribute("reservations", reservationService.allReservations());
        return "reservations/all";
    }

    // ================================================================
    // GET /reservations/new?roomId=1&participants=5
    // ================================================================
    @GetMapping("/new")
    public String newReservationPage(
            @RequestParam Long roomId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String startHour,
            @RequestParam(required = false) String duration,
            @RequestParam(required = false) Integer participants,
            Model model) {

        model.addAttribute("room", roomService.getRoomById(roomId));
        model.addAttribute("date", date);
        model.addAttribute("startHour", startHour);
        model.addAttribute("duration", duration);
        model.addAttribute("participants", participants);
        return "reservations/new";
    }

    // ================================================================
    // POST /reservations
    // Primește: roomId, date, startHour, endHour (calculat din JS),
    //           duration, participants, title, notes
    // ================================================================
    @PostMapping
    public String createReservation(
            @RequestParam Long roomId,
            @RequestParam String date,
            @RequestParam String startHour,
            @RequestParam String endHour,      // calculat de JS din start + duration
            @RequestParam String duration,
            @RequestParam Integer participants,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String notes,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            // Combini data cu orele
            LocalDate localDate = LocalDate.parse(date);
            LocalDateTime startTime = LocalDateTime.of(localDate, LocalTime.parse(startHour));
            LocalDateTime endTime   = LocalDateTime.of(localDate, LocalTime.parse(endHour));

            // Construiești DTO-ul
            ReservationRequestDTO dto = new ReservationRequestDTO();
            dto.setRoomId(roomId);
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setParticipants(participants);
            dto.setTitle(title);
            dto.setNotes(notes);

            reservationService.createReservation(dto);

            redirectAttributes.addFlashAttribute("success",
                    "Rezervarea a fost creată! " + startHour + " → " + endHour);
            return "redirect:/reservations/my";

        } catch (RuntimeException e) {
            // Sala ocupată, start după end, etc.
            model.addAttribute("error", e.getMessage());
            model.addAttribute("room", roomService.getRoomById(roomId));
            model.addAttribute("date", date);
            model.addAttribute("startHour", startHour);
            model.addAttribute("duration", duration);
            model.addAttribute("participants", participants);
            return "reservations/new";
        }
    }

    // ================================================================
    // POST /reservations/{id}/cancel
    // ================================================================
    @PostMapping("/{id}/cancel")
    public String cancelReservation(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        reservationService.deleteReservation(id);
        redirectAttributes.addFlashAttribute("success", "Rezervarea a fost anulată.");
        return "redirect:/reservations/my";
    }


    @GetMapping("/check-in/{id}")
    public String checkIn(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservationService.checkIn(id);
            System.out.println("Check-in realizat cu succes!");
            redirectAttributes.addFlashAttribute("successMessage", "Check-in realizat cu succes! Ședință plăcută.");

        } catch (RuntimeException e) {
            // Dacă apare o eroare, o trimitem tot către pagina principală ca mesaj de alertă
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/reservations/my";
    }
}
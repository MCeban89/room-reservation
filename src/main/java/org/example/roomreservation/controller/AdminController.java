package org.example.roomreservation.controller;

import lombok.AllArgsConstructor;
import org.example.roomreservation.model.entity.ReservationStatus;
import org.example.roomreservation.repository.RoomRepository;
import org.example.roomreservation.repository.UserRepository;
import org.example.roomreservation.service.ReservationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class AdminController {

    private final ReservationService reservationService;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @GetMapping
    public String adminDashboard(Model model) {

        // ── Utilizatori ─
        var users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("adminCount",
                users.stream().filter(u -> u.getRole().name().equals("ADMIN")).count());

        // ── Săli ──
        var rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        model.addAttribute("totalRooms", rooms.size());
        model.addAttribute("activeRooms",
                rooms.stream().filter(r -> Boolean.TRUE.equals(r.getActive())).count());

        // ── Rezervări ──
        var reservations = reservationService.allReservations();
        model.addAttribute("reservations", reservations);
        model.addAttribute("totalReservations", reservations.size());
        model.addAttribute("confirmedReservations",
                reservations.stream()
                        .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
                        .count());
        model.addAttribute("cancelledReservations",
                reservations.stream()
                        .filter(r -> r.getStatus() == ReservationStatus.CANCELLED)
                        .count());

        return "admin/index"; // templates/admin/index.html
    }
}
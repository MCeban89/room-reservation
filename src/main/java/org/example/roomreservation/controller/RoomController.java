package org.example.roomreservation.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.roomreservation.model.dto.RoomRequestDTO;
import org.example.roomreservation.model.entity.Room;
import org.example.roomreservation.service.RoomService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rooms")
@AllArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // ================================================================
    // GET /rooms — lista tuturor sălilor active
    // Accesat de: USER + ADMIN
    // ================================================================
    @GetMapping
    public String getRooms(Model model) {
        model.addAttribute("rooms", roomService.getRooms());
        return "rooms/list"; // caută templates/rooms/list.html
    }

    // ================================================================
    // GET /rooms/add — formular adăugare sală (doar ADMIN)
    // ================================================================
    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addRoomPage(Model model) {
        model.addAttribute("roomForm", new RoomRequestDTO());
        return "rooms/form"; // caută templates/rooms/form.html
    }

    // ================================================================
    // POST /rooms/add — procesează adăugarea sălii (doar ADMIN)
    // ================================================================
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addRoom(
            @Valid @ModelAttribute("roomForm") RoomRequestDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "rooms/form";
        }

        try {
            roomService.addRoom(dto);
            redirectAttributes.addFlashAttribute("success", "Sala a fost adăugată cu succes!");
            return "redirect:/rooms";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("name", "error.name", e.getMessage());
            return "rooms/form";
        }
    }

    // ================================================================
    // GET /rooms/{id}/edit — formular editare sală (doar ADMIN)
    // ================================================================
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editRoomPage(@PathVariable Long id, Model model) {
        Room room = roomService.getRoomById(id);

        // Populează formularul cu valorile existente
        RoomRequestDTO form = new RoomRequestDTO();
        form.setName(room.getName());
        form.setCapacity(room.getCapacity());
        form.setFloor(room.getFloor());
        form.setDescription(room.getDescription());
        form.setHasProjector(room.getHasProjector());
        form.setHasWhiteboard(room.getHasWhiteboard());
        form.setHasVideoConference(room.getHasVideoConference());

        model.addAttribute("room", room);       // pentru titlu și action URL
        model.addAttribute("roomForm", form);   // pentru th:field
        return "rooms/form";
    }

    // ================================================================
    // POST /rooms/{id}/edit — procesează editarea (doar ADMIN)
    // ================================================================
    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateRoom(
            @PathVariable Long id,
            @Valid @ModelAttribute("roomForm") RoomRequestDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "rooms/form";
        }

        roomService.updateRoom(id, dto);
        redirectAttributes.addFlashAttribute("success", "Sala a fost actualizată!");
        return "redirect:/rooms";
    }

    // ================================================================
    // POST /rooms/{id}/delete — dezactivare sală (soft delete, ADMIN)
    // Folosim POST în loc de DELETE pentru că HTML form-urile
    // nu suportă metoda DELETE nativ
    // ================================================================
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteRoom(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        roomService.deleteRoom(id);
        redirectAttributes.addFlashAttribute("success", "Sala a fost dezactivată!");
        return "redirect:/rooms";
    }
}
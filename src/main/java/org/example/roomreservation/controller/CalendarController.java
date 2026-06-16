package org.example.roomreservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CalendarController {

    @GetMapping("/calendar/{roomId}")
    public String showCalendar(@PathVariable Long roomId, Model model) {

        model.addAttribute("roomId", roomId);

        return "calendar";
    }
}

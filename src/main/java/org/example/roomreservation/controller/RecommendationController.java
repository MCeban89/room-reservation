package org.example.roomreservation.controller;

import lombok.AllArgsConstructor;
import org.example.roomreservation.service.RecommendationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@RequestMapping("/recommendations")
@AllArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping
    public String recommendationsPage(

            @RequestParam(required = false) String date,
            @RequestParam(required = false) String startHour,
            @RequestParam(required = false) String endHour,
            @RequestParam(required = false) String duration,

            @RequestParam(required = false) Integer participants,

            @RequestParam(defaultValue = "false") Boolean needsProjector,
            @RequestParam(defaultValue = "false") Boolean needsWhiteboard,
            @RequestParam(defaultValue = "false") Boolean needsVideoConference,

            Model model) {

        model.addAttribute("date", date);
        model.addAttribute("startHour", startHour);
        model.addAttribute("endHour", endHour);
        model.addAttribute("duration", duration);
        model.addAttribute("participants", participants);
        model.addAttribute("needsProjector", needsProjector);
        model.addAttribute("needsWhiteboard", needsWhiteboard);
        model.addAttribute("needsVideoConference", needsVideoConference);

        if (date != null && startHour != null && endHour != null && participants != null) {
            try {
                LocalDate localDate = LocalDate.parse(date);
                LocalDateTime startTime = LocalDateTime.of(localDate, LocalTime.parse(startHour));
                LocalDateTime endTime   = LocalDateTime.of(localDate, LocalTime.parse(endHour));

                model.addAttribute("recommendations",
                        recommendationService.getRecommendations(
                                startTime,
                                endTime,
                                participants,
                                needsProjector,
                                needsWhiteboard,
                                needsVideoConference));
            } catch (Exception e) {
                model.addAttribute("error", "Format invalid pentru dată sau oră.");
            }
        }

        return "recommendations/index";
    }
}
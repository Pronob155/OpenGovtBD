package com.opengovtbd.controller;

import com.opengovtbd.repository.AnnouncementRepository;
import com.opengovtbd.service.AnalyticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final AnalyticsService analyticsService;
    private final AnnouncementRepository announcementRepository;

    public HomeController(AnalyticsService analyticsService, AnnouncementRepository announcementRepository) {
        this.analyticsService = analyticsService;
        this.announcementRepository = announcementRepository;
    }

    @GetMapping("/")
    public String landing(Model model) {
        model.addAttribute("totalCitizens", analyticsService.totalCitizens());
        model.addAttribute("totalComplaints", analyticsService.totalComplaints());
        model.addAttribute("resolutionRate", Math.round(analyticsService.resolutionRate()));
        model.addAttribute("announcements", announcementRepository.findAll());
        return "index";
    }

    @GetMapping("/404")
    public String notFound() {
        return "error/404";
    }
}

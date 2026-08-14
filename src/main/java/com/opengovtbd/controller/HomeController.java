package com.opengovtbd.controller;

import com.opengovtbd.model.Role;
import com.opengovtbd.repository.AnnouncementRepository;
import com.opengovtbd.service.AnalyticsService;
import jakarta.servlet.http.HttpSession;
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
    public String notFound() { return "error/404"; }

    /** Sidebar "Credits" link — opens a dedicated credits page. */
    @GetMapping("/credits")
    public String credits(HttpSession session, Model model) {
        String homeUrl = "/";
        Object role = session.getAttribute("role");
        if (role instanceof Role) {
            switch ((Role) role) {
                case CITIZEN -> homeUrl = "/citizen/dashboard";
                case OFFICER -> homeUrl = "/officer/dashboard";
                case ADMIN -> homeUrl = "/admin/dashboard";
            }
        }
        model.addAttribute("homeUrl", homeUrl);
        return "credits";
    }
}

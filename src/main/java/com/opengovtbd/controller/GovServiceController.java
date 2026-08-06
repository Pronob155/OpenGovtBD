package com.opengovtbd.controller;

import com.opengovtbd.model.Admin;
import com.opengovtbd.model.Citizen;
import com.opengovtbd.model.GovService;
import com.opengovtbd.model.Officer;
import com.opengovtbd.service.AuthService;
import com.opengovtbd.service.GovServiceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GovServiceController {

    private final AuthService authService;
    private final GovServiceService govServiceService;

    public GovServiceController(AuthService authService, GovServiceService govServiceService) {
        this.authService = authService;
        this.govServiceService = govServiceService;
    }

    @GetMapping("/citizen/services/catalog")
    public String catalog(HttpSession session, Model model, @RequestParam(required = false) String category) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        model.addAttribute("citizen", citizen);
        model.addAttribute("services", govServiceService.byCategory(category));
        model.addAttribute("categories", govServiceService.categories());
        model.addAttribute("selectedCategory", category == null ? "all" : category);
        return "citizen/services-catalog";
    }

    @GetMapping("/officer/services")
    public String manage(HttpSession session, Model model) {
        Officer officer = SessionUser.requireOfficer(session, authService);
        model.addAttribute("officer", officer);
        model.addAttribute("services", govServiceService.all());
        return "officer/service-manage";
    }

    @PostMapping("/officer/services")
    public String create(@RequestParam String name, @RequestParam String description,
            @RequestParam String category, @RequestParam(required = false) String logoIcon,
            @RequestParam String externalUrl, HttpSession session) {
        Officer officer = SessionUser.requireOfficer(session, authService);
        govServiceService.create(name, description, category, logoIcon, externalUrl, officer);
        return "redirect:/officer/services?created=1";
    }

    @PostMapping("/officer/services/{id}")
    public String update(@PathVariable Long id, @RequestParam String name, @RequestParam String description,
            @RequestParam String category, @RequestParam(required = false) String logoIcon,
            @RequestParam String externalUrl, HttpSession session) {
        SessionUser.requireOfficer(session, authService);
        GovService service = govServiceService.find(id).orElseThrow();
        govServiceService.update(service, name, description, category, logoIcon, externalUrl);
        return "redirect:/officer/services?updated=1";
    }

    @PostMapping("/officer/services/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        SessionUser.requireOfficer(session, authService);
        govServiceService.delete(id);
        return "redirect:/officer/services?deleted=1";
    }

    @GetMapping("/admin/services")
    public String adminList(HttpSession session, Model model) {
        Admin admin = SessionUser.requireAdmin(session, authService);
        model.addAttribute("admin", admin);
        model.addAttribute("services", govServiceService.all());
        model.addAttribute("categories", govServiceService.categories());
        return "admin/services";
    }
}


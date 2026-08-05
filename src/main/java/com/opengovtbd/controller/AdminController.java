package com.opengovtbd.controller;

import com.opengovtbd.model.Admin;
import com.opengovtbd.model.User;
import com.opengovtbd.repository.UserRepository;
import com.opengovtbd.service.AnalyticsService;
import com.opengovtbd.service.AuthService;
import com.opengovtbd.service.DiscussionService;
import com.opengovtbd.service.GovServiceService;
import com.opengovtbd.service.SuggestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AuthService authService;
    private final AnalyticsService analyticsService;
    private final UserRepository userRepository;
    private final DiscussionService discussionService;
    private final SuggestionService suggestionService;
    private final GovServiceService govServiceService;

    public AdminController(AuthService authService, AnalyticsService analyticsService, UserRepository userRepository,
            DiscussionService discussionService, SuggestionService suggestionService,
            GovServiceService govServiceService) {
        this.authService = authService;
        this.analyticsService = analyticsService;
        this.userRepository = userRepository;
        this.discussionService = discussionService;
        this.suggestionService = suggestionService;
        this.govServiceService = govServiceService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Admin admin = SessionUser.requireAdmin(session, authService);
        model.addAttribute("admin", admin);
        model.addAttribute("analytics", analyticsService);
        model.addAttribute("citizens", userRepository.findAllCitizens());
        model.addAttribute("officers", userRepository.findAllOfficers());
        model.addAttribute("servicesCount", govServiceService.count());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(HttpSession session, Model model, @RequestParam(required = false) String phone) {
        model.addAttribute("admin", SessionUser.requireAdmin(session, authService));
        model.addAttribute("citizens", phone == null || phone.isBlank()
                ? userRepository.findAllCitizens()
                : userRepository.searchCitizensByPhone(phone));
        model.addAttribute("officers", userRepository.findAllOfficers());
        model.addAttribute("phoneQuery", phone == null ? "" : phone);
        return "admin/users";
    }

    @GetMapping("/users/{id}")
    public String userDetail(@PathVariable Long id, HttpSession session, Model model) {
        SessionUser.requireAdmin(session, authService);
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("admin", SessionUser.requireAdmin(session, authService));
        model.addAttribute("targetUser", user);
        return "admin/user-detail";
    }

    @PostMapping("/users/{id}/toggle-active")
    public String toggleActive(@PathVariable Long id, HttpSession session) {
        SessionUser.requireAdmin(session, authService);
        userRepository.findById(id).ifPresent(User::reinstate);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/suspend")
    public String suspend(@PathVariable Long id, @RequestParam String endDate,
            @RequestParam(required = false) String reason,
            HttpSession session, RedirectAttributes redirectAttributes) {
        Admin admin = SessionUser.requireAdmin(session, authService);
        User user = userRepository.findById(id).orElseThrow();
        user.suspend(LocalDate.parse(endDate), reason == null || reason.isBlank() ? "Policy violation" : reason,
                admin.getFullName());
        redirectAttributes.addFlashAttribute("success", user.isBanned()
                ? user.getFullName() + " has been permanently banned after repeated suspensions."
                : user.getFullName() + " has been suspended until " + endDate + ".");
        return "redirect:/admin/users/" + id;
    }

    @PostMapping("/users/{id}/ban")
    public String ban(@PathVariable Long id, @RequestParam(required = false) String reason,
            HttpSession session, RedirectAttributes redirectAttributes) {
        Admin admin = SessionUser.requireAdmin(session, authService);
        User user = userRepository.findById(id).orElseThrow();
        user.ban(reason == null || reason.isBlank() ? "Permanently banned by admin" : reason, admin.getFullName());
        redirectAttributes.addFlashAttribute("success", user.getFullName() + " has been permanently banned.");
        return "redirect:/admin/users/" + id;
    }

    @PostMapping("/users/{id}/reinstate")
    public String reinstate(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        SessionUser.requireAdmin(session, authService);
        User user = userRepository.findById(id).orElseThrow();
        user.reinstate();
        redirectAttributes.addFlashAttribute("success", user.getFullName() + "'s account has been reinstated.");
        return "redirect:/admin/users/" + id;
    }

    @GetMapping("/analytics")
    public String analytics(HttpSession session, Model model) {
        model.addAttribute("admin", SessionUser.requireAdmin(session, authService));
        model.addAttribute("analytics", analyticsService);
        return "admin/analytics";
    }

    @GetMapping("/discussions")
    public String discussions(HttpSession session, Model model) {
        model.addAttribute("admin", SessionUser.requireAdmin(session, authService));
        model.addAttribute("discussions", discussionService.approvedFeed(null, null, null));
        model.addAttribute("pending", discussionService.pendingApproval());
        return "admin/discussions";
    }

    @GetMapping("/reports")
    public String reports(HttpSession session, Model model) {
        model.addAttribute("admin", SessionUser.requireAdmin(session, authService));
        model.addAttribute("analytics", analyticsService);
        model.addAttribute("suggestions", suggestionService.all());
        return "admin/reports";
    }
}

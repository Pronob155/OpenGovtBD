package com.opengovtbd.controller;

import com.opengovtbd.model.Citizen;
import com.opengovtbd.model.Officer;
import com.opengovtbd.model.Suggestion;
import com.opengovtbd.model.SuggestionStatus;
import com.opengovtbd.repository.UserRepository;
import com.opengovtbd.service.AuthService;
import com.opengovtbd.service.SuggestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SuggestionController {

    private final AuthService authService;
    private final SuggestionService suggestionService;
    private final UserRepository userRepository;

    public SuggestionController(AuthService authService, SuggestionService suggestionService, UserRepository userRepository) {
        this.authService = authService;
        this.suggestionService = suggestionService;
        this.userRepository = userRepository;
    }

    @GetMapping("/citizen/suggestions")
    public String list(HttpSession session, Model model) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        model.addAttribute("citizen", citizen);
        model.addAttribute("suggestions", suggestionService.all());
        model.addAttribute("userRepository", userRepository);
        return "citizen/suggestions";
    }

    @PostMapping("/citizen/suggestions/new")
    public String submit(@RequestParam String title, @RequestParam String description, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        suggestionService.submit(citizen, title, description);
        return "redirect:/citizen/suggestions?submitted=1";
    }

    @GetMapping("/citizen/suggestions/{id}")
    public String details(@PathVariable Long id, HttpSession session, Model model) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        Suggestion suggestion = suggestionService.find(id).orElseThrow();
        model.addAttribute("citizen", citizen);
        model.addAttribute("suggestion", suggestion);
        model.addAttribute("userRepository", userRepository);
        return "citizen/suggestion-details";
    }

    @PostMapping("/citizen/suggestions/{id}/upvote")
    public String upvote(@PathVariable Long id, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        suggestionService.upvote(suggestionService.find(id).orElseThrow(), citizen.getId());
        return "redirect:/citizen/suggestions";
    }

    @PostMapping("/citizen/suggestions/{id}/downvote")
    public String downvote(@PathVariable Long id, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        suggestionService.downvote(suggestionService.find(id).orElseThrow(), citizen.getId());
        return "redirect:/citizen/suggestions";
    }

    @PostMapping("/citizen/suggestions/{id}/comment")
    public String comment(@PathVariable Long id, @RequestParam String content, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        suggestionService.addComment(suggestionService.find(id).orElseThrow(), citizen.getId(), content);
        return "redirect:/citizen/suggestions/" + id;
    }

    @GetMapping("/officer/suggestions")
    public String review(HttpSession session, Model model) {
        Officer officer = SessionUser.requireOfficer(session, authService);
        model.addAttribute("officer", officer);
        model.addAttribute("suggestions", suggestionService.all());
        model.addAttribute("userRepository", userRepository);
        return "officer/suggestion-review";
    }

    @PostMapping("/officer/suggestions/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam SuggestionStatus status,
                                @RequestParam String feedback, HttpSession session) {
        SessionUser.requireOfficer(session, authService);
        suggestionService.updateStatus(suggestionService.find(id).orElseThrow(), status, feedback);
        return "redirect:/officer/suggestions";
    }
}

package com.opengovtbd.controller;

import com.opengovtbd.model.Citizen;
import com.opengovtbd.model.Officer;
import com.opengovtbd.model.Poll;
import com.opengovtbd.service.AuthService;
import com.opengovtbd.service.PollService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
public class PollController {

    private final AuthService authService;
    private final PollService pollService;

    public PollController(AuthService authService, PollService pollService) {
        this.authService = authService;
        this.pollService = pollService;
    }

    @GetMapping("/citizen/polls")
    public String polls(HttpSession session, Model model) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        model.addAttribute("citizen", citizen);
        model.addAttribute("activePolls", pollService.active());
        model.addAttribute("archivedPolls", pollService.archived());
        return "citizen/polls";
    }

    @PostMapping("/citizen/polls/{id}/vote")
    public String vote(@PathVariable Long id, @RequestParam int optionIndex, HttpSession session,
            Model model, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        Poll poll = pollService.find(id).orElseThrow();
        try {
            pollService.vote(poll, poll.getOptions().get(optionIndex), citizen);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/citizen/polls";
    }

    @PostMapping("/citizen/polls/{id}/bookmark")
    public String bookmark(@PathVariable Long id, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        pollService.toggleBookmark(pollService.find(id).orElseThrow(), citizen.getId());
        return "redirect:/citizen/polls";
    }

    // Officer: create a new official poll
    @GetMapping("/officer/polls/new")
    public String newPollForm(HttpSession session, Model model) {
        model.addAttribute("officer", SessionUser.requireOfficer(session, authService));
        return "officer/poll-form";
    }

    @PostMapping("/officer/polls/new")
    public String createPoll(@RequestParam String question, @RequestParam String category,
            @RequestParam String options, @RequestParam(required = false) Integer daysOpen,
            HttpSession session) {
        SessionUser.requireOfficer(session, authService);
        List<String> optionList = Arrays.stream(options.split("\\r?\\n"))
                .map(String::trim).filter(s -> !s.isEmpty()).toList();
        LocalDateTime deadline = LocalDateTime.now().plusDays(daysOpen == null ? 7 : daysOpen);
        Poll poll = pollService.create(question, category, optionList, deadline);
        return "redirect:/officer/dashboard?pollCreated=" + poll.getId();
    }
}


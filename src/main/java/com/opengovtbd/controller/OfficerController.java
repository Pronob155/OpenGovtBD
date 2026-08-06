package com.opengovtbd.controller;

import com.opengovtbd.model.Officer;
import com.opengovtbd.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/officer")
public class OfficerController {

    private final AuthService authService;
    private final ComplaintService complaintService;
    private final DiscussionService discussionService;
    private final SuggestionService suggestionService;
    private final PollService pollService;
    private final GovServiceService govServiceService;
    private final RewardService rewardService;

    public OfficerController(AuthService authService, ComplaintService complaintService,
            DiscussionService discussionService, SuggestionService suggestionService,
            PollService pollService, GovServiceService govServiceService,
            RewardService rewardService) {
        this.authService = authService;
        this.complaintService = complaintService;
        this.discussionService = discussionService;
        this.suggestionService = suggestionService;
        this.pollService = pollService;
        this.govServiceService = govServiceService;
        this.rewardService = rewardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Officer officer = SessionUser.requireOfficer(session, authService);
        model.addAttribute("officer", officer);
        model.addAttribute("complaints", complaintService.all());
        model.addAttribute("pendingDiscussions", discussionService.pendingApproval());
        model.addAttribute("suggestions", suggestionService.all());
        model.addAttribute("activePolls", pollService.active());
        model.addAttribute("totalComplaints", complaintService.total());
        model.addAttribute("resolvedComplaints", complaintService.resolved());
        model.addAttribute("pendingComplaints", complaintService.pendingBucket());
        model.addAttribute("awaitingComplaints", complaintService.awaitingBucket());
        model.addAttribute("servicesCount", govServiceService.count());
        model.addAttribute("officerPoints", rewardService.pointsForOfficer(officer.getId()));
        model.addAttribute("recentActivity", rewardService.recentActivityFor(officer.getId(), 6));
        return "officer/dashboard";
    }
}

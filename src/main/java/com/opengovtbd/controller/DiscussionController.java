package com.opengovtbd.controller;

import com.opengovtbd.model.Citizen;
import com.opengovtbd.model.Discussion;
import com.opengovtbd.model.Officer;
import com.opengovtbd.repository.UserRepository;
import com.opengovtbd.service.AuthService;
import com.opengovtbd.service.DiscussionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DiscussionController {

    private final AuthService authService;
    private final DiscussionService discussionService;
    private final UserRepository userRepository;

    public DiscussionController(AuthService authService, DiscussionService discussionService, UserRepository userRepository) {
        this.authService = authService;
        this.discussionService = discussionService;
        this.userRepository = userRepository;
    }

    @GetMapping("/citizen/discussions")
    public String feed(HttpSession session, Model model,
                        @RequestParam(required = false) String sort,
                        @RequestParam(required = false) String category,
                        @RequestParam(required = false) String q) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        model.addAttribute("citizen", citizen);
        model.addAttribute("discussions", discussionService.approvedFeed(sort, category, q));
        model.addAttribute("sort", sort == null ? "latest" : sort);
        model.addAttribute("category", category == null ? "all" : category);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("userRepository", userRepository);
        return "citizen/discussions";
    }

    @PostMapping("/citizen/discussions/new")
    public String create(@RequestParam String title, @RequestParam String content,
                          @RequestParam String category, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        discussionService.create(citizen, title, content, category);
        return "redirect:/citizen/discussions?created=1";
    }

    @GetMapping("/citizen/discussions/{id}")
    public String details(@PathVariable Long id, HttpSession session, Model model) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        Discussion discussion = discussionService.find(id).orElseThrow();
        model.addAttribute("citizen", citizen);
        model.addAttribute("discussion", discussion);
        model.addAttribute("author", userRepository.findById(discussion.getAuthorId()).orElse(null));
        model.addAttribute("userRepository", userRepository);
        return "citizen/discussion-details";
    }

    @PostMapping("/citizen/discussions/{id}/like")
    public String like(@PathVariable Long id, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        discussionService.toggleLike(discussionService.find(id).orElseThrow(), citizen.getId());
        return "redirect:/citizen/discussions/" + id;
    }

    @PostMapping("/citizen/discussions/{id}/dislike")
    public String dislike(@PathVariable Long id, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        discussionService.toggleDislike(discussionService.find(id).orElseThrow(), citizen.getId());
        return "redirect:/citizen/discussions/" + id;
    }

    @PostMapping("/citizen/discussions/{id}/bookmark")
    public String bookmark(@PathVariable Long id, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        discussionService.toggleBookmark(discussionService.find(id).orElseThrow(), citizen.getId());
        return "redirect:/citizen/discussions/" + id;
    }

    @PostMapping("/citizen/discussions/{id}/comment")
    public String comment(@PathVariable Long id, @RequestParam String content,
                           @RequestParam(required = false) Long parentId, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        discussionService.addComment(discussionService.find(id).orElseThrow(), citizen.getId(), content, parentId);
        return "redirect:/citizen/discussions/" + id + "#comments";
    }

    @PostMapping("/citizen/discussions/{id}/comment/{commentId}/like")
    public String likeComment(@PathVariable Long id, @PathVariable Long commentId, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        discussionService.toggleCommentLike(discussionService.find(id).orElseThrow(), commentId, citizen.getId());
        return "redirect:/citizen/discussions/" + id + "#comments";
    }

    @GetMapping("/officer/discussions")
    public String moderation(HttpSession session, Model model) {
        Officer officer = SessionUser.requireOfficer(session, authService);
        model.addAttribute("officer", officer);
        model.addAttribute("pending", discussionService.pendingApproval());
        model.addAttribute("approved", discussionService.approvedFeed(null, null, null));
        model.addAttribute("userRepository", userRepository);
        return "officer/discussion-approval";
    }

    @PostMapping("/officer/discussions/{id}/approve")
    public String approve(@PathVariable Long id, HttpSession session) {
        SessionUser.requireOfficer(session, authService);
        discussionService.approve(discussionService.find(id).orElseThrow());
        return "redirect:/officer/discussions";
    }

    @PostMapping("/officer/discussions/{id}/reject")
    public String reject(@PathVariable Long id, HttpSession session) {
        SessionUser.requireOfficer(session, authService);
        discussionService.reject(discussionService.find(id).orElseThrow());
        return "redirect:/officer/discussions";
    }

    @PostMapping("/officer/discussions/{id}/pin")
    public String pin(@PathVariable Long id, HttpSession session) {
        SessionUser.requireOfficer(session, authService);
        discussionService.togglePin(discussionService.find(id).orElseThrow());
        return "redirect:/officer/discussions";
    }

    @PostMapping("/officer/discussions/{id}/lock")
    public String lock(@PathVariable Long id, HttpSession session) {
        SessionUser.requireOfficer(session, authService);
        discussionService.toggleLock(discussionService.find(id).orElseThrow());
        return "redirect:/officer/discussions";
    }

    @PostMapping("/officer/discussions/{id}/respond")
    public String respond(@PathVariable Long id, @RequestParam String response, HttpSession session) {
        SessionUser.requireOfficer(session, authService);
        discussionService.setOfficialResponse(discussionService.find(id).orElseThrow(), response);
        return "redirect:/officer/discussions";
    }
}

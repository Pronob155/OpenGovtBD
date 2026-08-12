package com.opengovtbd.controller;

import com.opengovtbd.model.Citizen;
import com.opengovtbd.model.Complaint;
import com.opengovtbd.model.Officer;
import com.opengovtbd.model.ComplaintStatus;
import com.opengovtbd.repository.UserRepository;
import com.opengovtbd.service.AuthService;
import com.opengovtbd.service.ComplaintService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ComplaintController {

    private final AuthService authService;
    private final ComplaintService complaintService;
    private final UserRepository userRepository;

    public ComplaintController(AuthService authService, ComplaintService complaintService, UserRepository userRepository) {
        this.authService = authService;
        this.complaintService = complaintService;
        this.userRepository = userRepository;
    }

    // ---------- Citizen side ----------

    @GetMapping("/citizen/complaints")
    public String list(HttpSession session, Model model) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        model.addAttribute("citizen", citizen);
        model.addAttribute("complaints", complaintService.forCitizen(citizen.getId()));
        return "citizen/complaints";
    }

    @GetMapping("/citizen/complaints/new")
    public String newForm(HttpSession session, Model model) {
        model.addAttribute("citizen", SessionUser.requireCitizen(session, authService));
        return "citizen/complaint-form";
    }

    @PostMapping("/citizen/complaints/new")
    public String submit(@RequestParam String title, @RequestParam String description,
                          @RequestParam String category, @RequestParam String division,
                          @RequestParam String district, @RequestParam String upazila,
                          HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        Complaint complaint = complaintService.submit(citizen, title, description, category, division, district, upazila);
        return "redirect:/citizen/complaints/" + complaint.getId() + "?submitted=1";
    }

    @GetMapping("/citizen/complaints/{id}")
    public String details(@PathVariable Long id, HttpSession session, Model model,
                           @RequestParam(required = false) String submitted) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        Complaint complaint = complaintService.find(id).orElseThrow();
        model.addAttribute("citizen", citizen);
        model.addAttribute("complaint", complaint);
        model.addAttribute("submitted", submitted != null);
        model.addAttribute("userRepository", userRepository);
        if (complaint.getAssignedOfficerId() != null) {
            userRepository.findById(complaint.getAssignedOfficerId()).ifPresent(o -> model.addAttribute("officer", o));
        }
        return "citizen/complaint-details";
    }

    @PostMapping("/citizen/complaints/{id}/rate")
    public String rate(@PathVariable Long id, @RequestParam int rating,
                        @RequestParam(required = false) String feedback, HttpSession session) {
        SessionUser.requireCitizen(session, authService);
        Complaint complaint = complaintService.find(id).orElseThrow();
        complaintService.rateResolution(complaint, rating, feedback == null ? "" : feedback);
        return "redirect:/citizen/complaints/" + id;
    }

    @PostMapping("/citizen/complaints/{id}/reopen")
    public String reopen(@PathVariable Long id, @RequestParam String reason, HttpSession session) {
        SessionUser.requireCitizen(session, authService);
        Complaint complaint = complaintService.find(id).orElseThrow();
        complaintService.reopen(complaint, reason);
        return "redirect:/citizen/complaints/" + id;
    }

    @PostMapping("/citizen/complaints/{id}/bookmark")
    public String bookmark(@PathVariable Long id, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        complaintService.toggleBookmark(complaintService.find(id).orElseThrow(), citizen.getId());
        return "redirect:/citizen/complaints/" + id;
    }

    @PostMapping("/citizen/complaints/{id}/comment")
    public String comment(@PathVariable Long id, @RequestParam String content,
                           @RequestParam(required = false) Long parentId, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        complaintService.addComment(complaintService.find(id).orElseThrow(), citizen.getId(), content, parentId);
        return "redirect:/citizen/complaints/" + id + "#comments";
    }

    @PostMapping("/citizen/complaints/{id}/comment/{commentId}/like")
    public String likeComment(@PathVariable Long id, @PathVariable Long commentId, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        complaintService.toggleCommentLike(complaintService.find(id).orElseThrow(), commentId, citizen.getId());
        return "redirect:/citizen/complaints/" + id + "#comments";
    }

    // ---------- Officer side ----------

    @GetMapping("/officer/complaints")
    public String queue(HttpSession session, Model model,
                         @RequestParam(required = false) String status,
                         @RequestParam(required = false) String filter) {
        Officer officer = SessionUser.requireOfficer(session, authService);
        model.addAttribute("officer", officer);
        var all = complaintService.all();
        if (filter != null && !filter.isBlank() && !filter.equalsIgnoreCase("total")) {
            if (filter.equalsIgnoreCase("pending")) {
                all = all.stream().filter(c -> c.getStatus() == ComplaintStatus.SUBMITTED || c.getStatus() == ComplaintStatus.PENDING).toList();
            } else if (filter.equalsIgnoreCase("awaiting")) {
                all = all.stream().filter(c -> c.getStatus() == ComplaintStatus.ASSIGNED || c.getStatus() == ComplaintStatus.UNDER_REVIEW
                        || c.getStatus() == ComplaintStatus.IN_PROGRESS || c.getStatus() == ComplaintStatus.WAITING_FOR_CITIZEN).toList();
            } else if (filter.equalsIgnoreCase("resolved")) {
                all = all.stream().filter(Complaint::isResolved).toList();
            }
        } else if (status != null && !status.isBlank() && !status.equalsIgnoreCase("all")) {
            all = all.stream().filter(c -> c.getStatus().name().equalsIgnoreCase(status)).toList();
        }
        model.addAttribute("complaints", all);
        model.addAttribute("statuses", ComplaintStatus.values());
        model.addAttribute("selectedStatus", status == null ? "all" : status);
        model.addAttribute("selectedFilter", filter == null ? "total" : filter);
        return "officer/complaint-queue";
    }

    @GetMapping("/officer/complaints/{id}")
    public String officerDetails(@PathVariable Long id, HttpSession session, Model model) {
        Officer officer = SessionUser.requireOfficer(session, authService);
        Complaint complaint = complaintService.find(id).orElseThrow();
        model.addAttribute("officer", officer);
        model.addAttribute("complaint", complaint);
        model.addAttribute("statuses", ComplaintStatus.values());
        userRepository.findById(complaint.getCitizenId()).ifPresent(c -> model.addAttribute("citizen", c));
        model.addAttribute("otherOfficers", userRepository.findAllOfficers().stream()
                .filter(o -> !o.getId().equals(complaint.getAssignedOfficerId()))
                .toList());
        return "officer/complaint-details";
    }

    @PostMapping("/officer/complaints/{id}/assign")
    public String assign(@PathVariable Long id, HttpSession session) {
        Officer officer = SessionUser.requireOfficer(session, authService);
        Complaint complaint = complaintService.find(id).orElseThrow();
        complaintService.assign(complaint, officer);
        return "redirect:/officer/complaints/" + id;
    }

    @PostMapping("/officer/complaints/{id}/reassign")
    public String reassign(@PathVariable Long id, @RequestParam Long toOfficerId,
                            @RequestParam(required = false) String reason, HttpSession session) {
        Officer fromOfficer = SessionUser.requireOfficer(session, authService);
        Complaint complaint = complaintService.find(id).orElseThrow();
        Officer toOfficer = userRepository.findAllOfficers().stream()
                .filter(o -> o.getId().equals(toOfficerId))
                .findFirst()
                .orElseThrow();
        complaintService.reassign(complaint, fromOfficer, toOfficer, reason);
        return "redirect:/officer/complaints/" + id;
    }

    @PostMapping("/officer/complaints/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam ComplaintStatus status,
                                @RequestParam(required = false) String note, HttpSession session) {
        Officer officer = SessionUser.requireOfficer(session, authService);
        Complaint complaint = complaintService.find(id).orElseThrow();
        complaintService.changeStatus(complaint, status, note == null ? "" : note, officer);
        return "redirect:/officer/complaints/" + id;
    }

    @PostMapping("/officer/complaints/{id}/reply")
    public String reply(@PathVariable Long id, @RequestParam String message, HttpSession session) {
        Officer officer = SessionUser.requireOfficer(session, authService);
        Complaint complaint = complaintService.find(id).orElseThrow();
        complaintService.reply(complaint, message, officer);
        return "redirect:/officer/complaints/" + id;
    }
}

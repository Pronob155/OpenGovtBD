package com.opengovtbd.controller;

import com.opengovtbd.model.Citizen;
import com.opengovtbd.repository.AnnouncementRepository;
import com.opengovtbd.repository.UserRepository;
import com.opengovtbd.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/citizen")
public class CitizenController {

    private final AuthService authService;
    private final ComplaintService complaintService;
    private final DiscussionService discussionService;
    private final PollService pollService;
    private final SuggestionService suggestionService;
    private final NotificationService notificationService;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final RewardService rewardService;
    private final FileStorageService fileStorageService;

    public CitizenController(AuthService authService, ComplaintService complaintService,
                              DiscussionService discussionService, PollService pollService,
                              SuggestionService suggestionService, NotificationService notificationService,
                              AnnouncementRepository announcementRepository, UserRepository userRepository,
                              RewardService rewardService, FileStorageService fileStorageService) {
        this.authService = authService;
        this.complaintService = complaintService;
        this.discussionService = discussionService;
        this.pollService = pollService;
        this.suggestionService = suggestionService;
        this.notificationService = notificationService;
        this.announcementRepository = announcementRepository;
        this.userRepository = userRepository;
        this.rewardService = rewardService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        var complaints = complaintService.forCitizen(citizen.getId());
        model.addAttribute("citizen", citizen);
        model.addAttribute("complaints", complaints);
        model.addAttribute("resolvedCount", complaints.stream().filter(com.opengovtbd.model.Complaint::isResolved).count());
        model.addAttribute("activePolls", pollService.active());
        model.addAttribute("discussions", discussionService.approvedFeed(null, null, null).stream().limit(3).toList());
        model.addAttribute("suggestions", suggestionService.forCitizen(citizen.getId()));
        model.addAttribute("announcements", announcementRepository.findAll());
        model.addAttribute("notifications", notificationService.forUser(citizen.getId()).stream().limit(5).toList());
        model.addAttribute("unreadCount", notificationService.unreadCount(citizen.getId()));

        // Leaderboard preview (Component 3b): reuse the real ranked-citizen data that
        // powers the full /citizen/leaderboard page.
        List<Citizen> allRanked = userRepository.findAllCitizens().stream()
                .sorted(Comparator.comparingInt(Citizen::getPoints).reversed())
                .collect(Collectors.toList());
        int topN = Math.min(5, allRanked.size());
        List<Citizen> leaderboardTop = allRanked.subList(0, topN);
        int myRank = 0;
        boolean meInTop = false;
        for (int i = 0; i < allRanked.size(); i++) {
            if (allRanked.get(i).getId().equals(citizen.getId())) {
                myRank = i + 1;
                meInTop = i < topN;
                break;
            }
        }
        model.addAttribute("leaderboardTop", leaderboardTop);
        model.addAttribute("leaderboardMyRank", myRank);
        model.addAttribute("leaderboardMeInTop", meInTop);
        return "citizen/dashboard";
    }

    @GetMapping("/announcements/{id}")
    public String announcementDetails(@PathVariable Long id, HttpSession session, Model model) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        var announcement = announcementRepository.findById(id).orElse(null);
        if (announcement == null) {
            return "error/404";
        }
        model.addAttribute("citizen", citizen);
        model.addAttribute("announcement", announcement);
        model.addAttribute("unreadCount", notificationService.unreadCount(citizen.getId()));
        return "citizen/announcement-details";
    }

    @GetMapping("/services")
    public String services(HttpSession session, Model model) {
        model.addAttribute("citizen", SessionUser.requireCitizen(session, authService));
        return "citizen/services";
    }

    @GetMapping("/emergency")
    public String emergency(HttpSession session, Model model) {
        model.addAttribute("citizen", SessionUser.requireCitizen(session, authService));
        return "citizen/emergency";
    }

    @GetMapping("/notifications")
    public String notifications(HttpSession session, Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(required = false) String category) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        List<com.opengovtbd.model.Notification> all = notificationService.forUser(citizen.getId());
        if (category != null && !category.isBlank() && !category.equalsIgnoreCase("all")) {
            all = all.stream().filter(n -> n.getType().name().equalsIgnoreCase(category)).collect(Collectors.toList());
        }
        int pageSize = 8;
        int totalPages = Math.max(1, (int) Math.ceil(all.size() / (double) pageSize));
        page = Math.max(0, Math.min(page, totalPages - 1));
        List<com.opengovtbd.model.Notification> pageItems = all.stream()
                .skip((long) page * pageSize).limit(pageSize).collect(Collectors.toList());
        model.addAttribute("citizen", citizen);
        model.addAttribute("notifications", pageItems);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("category", category == null ? "all" : category);
        model.addAttribute("types", com.opengovtbd.model.Notification.Type.values());
        return "citizen/notifications";
    }

    @PostMapping("/notifications/mark-all-read")
    public String markAllRead(HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        notificationService.markAllRead(citizen.getId());
        return "redirect:/citizen/notifications";
    }

    /** Used by the notification center's client-side JS to clear a single unread dot without a full page reload. */
    @PostMapping("/notifications/{id}/read")
    @ResponseBody
    public java.util.Map<String, Boolean> markOneRead(@PathVariable Long id, HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        notificationService.markRead(id, citizen.getId());
        return java.util.Map.of("ok", true);
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        model.addAttribute("citizen", citizen);
        model.addAttribute("complaints", complaintService.forCitizen(citizen.getId()));
        model.addAttribute("suggestions", suggestionService.forCitizen(citizen.getId()));
        model.addAttribute("savedDiscussions", discussionService.savedBy(citizen.getId()));
        model.addAttribute("savedPolls", pollService.savedBy(citizen.getId()));
        model.addAttribute("savedComplaints", complaintService.savedBy(citizen.getId()));
        return "citizen/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam String fullName, @RequestParam(required = false) String address,
                                 @RequestParam(required = false) String division,
                                 @RequestParam(required = false) String email,
                                 @RequestParam(required = false) String secondaryPhone,
                                 HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        citizen.setFullName(fullName);
        citizen.setAddress(address);
        citizen.setDivision(division);
        citizen.setEmail(email);
        citizen.setSecondaryPhone(secondaryPhone);
        session.setAttribute("fullName", citizen.getFullName());
        return "redirect:/citizen/profile?updated=1";
    }

    @PostMapping("/profile/preferences")
    public String updatePreferences(@RequestParam(required = false) String language,
                                     @RequestParam(required = false) String darkMode,
                                     HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        citizen.setLanguage(language == null ? "en" : language);
        citizen.setDarkMode("on".equals(darkMode));
        return "redirect:/citizen/profile";
    }

    @PostMapping("/theme/toggle")
    public String toggleTheme(HttpSession session, HttpServletRequest request) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        citizen.setDarkMode(!citizen.isDarkMode());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null && !referer.isBlank() ? referer : "/citizen/dashboard");
    }

    // ---------- Identity verification (NID + selfie, simulated AI check) ----------

    @GetMapping("/verification")
    public String verificationPage(HttpSession session, Model model) {
        model.addAttribute("citizen", SessionUser.requireCitizen(session, authService));
        return "citizen/verification";
    }

    @PostMapping("/verification/submit")
    public String submitVerification(@RequestParam MultipartFile nidImage, @RequestParam MultipartFile selfie,
                                      HttpSession session) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        String nidPath = fileStorageService.store(nidImage, "nid");
        String selfiePath = fileStorageService.store(selfie, "selfie");
        citizen.setNidImagePath(nidPath);
        citizen.setSelfieImagePath(selfiePath);
        citizen.setVerificationSubmittedAt(java.time.LocalDateTime.now());
        citizen.setVerificationStatus(Citizen.VerificationStatus.PENDING);
        // Simulated AI verification: in this demo, a well-formed submission (both files present)
        // is instantly "approved" — in production this would call a real face-match/NID OCR service.
        if (nidPath != null && selfiePath != null) {
            citizen.markVerified();
            rewardService.award(citizen, RewardService.POINTS_COMPLAINT, "Completed identity verification");
            notificationService.notify(citizen.getId(), "Your identity has been verified. You now have the Verified badge!",
                    com.opengovtbd.model.Notification.Type.SECURITY, "/citizen/profile");
        } else {
            citizen.setVerificationStatus(Citizen.VerificationStatus.REJECTED);
        }
        return "redirect:/citizen/verification?submitted=1";
    }

    // ---------- Saved items ----------

    @GetMapping("/saved")
    public String saved(HttpSession session, Model model) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        model.addAttribute("citizen", citizen);
        model.addAttribute("savedDiscussions", discussionService.savedBy(citizen.getId()));
        model.addAttribute("savedPolls", pollService.savedBy(citizen.getId()));
        model.addAttribute("savedComplaints", complaintService.savedBy(citizen.getId()));
        model.addAttribute("userRepository", userRepository);
        return "citizen/saved";
    }

    @GetMapping("/activity")
    public String activity(HttpSession session, Model model) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        model.addAttribute("citizen", citizen);

        List<com.opengovtbd.model.ActivityItem> items = new java.util.ArrayList<>();

        for (var c : complaintService.forCitizen(citizen.getId())) {
            items.add(new com.opengovtbd.model.ActivityItem(
                    "Complaint", "report", c.getTitle(),
                    c.getStatus().getLabel(), c.getStatus().getTone(),
                    c.getCreatedAt(), "/citizen/complaints/" + c.getId()));
        }

        for (var d : discussionService.forCitizen(citizen.getId())) {
            String statusLabel = d.isApproved() ? "Published" : "Pending Approval";
            String statusTone = d.isApproved() ? "success" : "warning";
            items.add(new com.opengovtbd.model.ActivityItem(
                    "Discussion", "forum", d.getTitle(),
                    statusLabel, statusTone,
                    d.getCreatedAt(),
                    d.isApproved() ? "/citizen/discussions/" + d.getId() : "/citizen/activity"));
        }

        for (var s : suggestionService.forCitizen(citizen.getId())) {
            items.add(new com.opengovtbd.model.ActivityItem(
                    "Suggestion", "lightbulb", s.getTitle(),
                    s.getStatus().getLabel(), s.getStatus().getTone(),
                    s.getCreatedAt(), "/citizen/suggestions/" + s.getId()));
        }

        items.sort(Comparator.comparing(com.opengovtbd.model.ActivityItem::getTimestamp).reversed());
        model.addAttribute("items", items);
        return "citizen/activity";
    }

    @GetMapping("/leaderboard")
    public String leaderboard(HttpSession session, Model model,
                              @RequestParam(required = false) String period,
                              @RequestParam(required = false) Long focusId) {
        Citizen citizen = SessionUser.requireCitizen(session, authService);
        model.addAttribute("citizen", citizen);
        List<Citizen> ranked = userRepository.findAllCitizens().stream()
                .sorted(Comparator.comparingInt(Citizen::getPoints).reversed())
                .collect(Collectors.toList());
        model.addAttribute("ranked", ranked);
        model.addAttribute("recentActivity", rewardService.recentActivityFor(citizen.getId(), 8));
        model.addAttribute("officerRanked", userRepository.findAllOfficers().stream()
                .sorted(Comparator.comparingInt(o -> -rewardService.pointsForOfficer(o.getId())))
                .collect(Collectors.toList()));
        model.addAttribute("rewardService", rewardService);
        model.addAttribute("period", period == null ? "month" : period);
        if (focusId != null) {
            model.addAttribute("selectedCitizen", ranked.stream().filter(c -> c.getId().equals(focusId)).findFirst().orElse(null));
        }

        int myRank = 0;
        for (int i = 0; i < ranked.size(); i++) {
            if (ranked.get(i).getId().equals(citizen.getId())) {
                myRank = i + 1;
                break;
            }
        }
        model.addAttribute("myRank", myRank);
        return "citizen/leaderboard";
    }
}

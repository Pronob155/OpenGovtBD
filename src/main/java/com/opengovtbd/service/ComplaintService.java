package com.opengovtbd.service;

import com.opengovtbd.model.*;
import com.opengovtbd.repository.ComplaintRepository;
import com.opengovtbd.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final NotificationService notificationService;
    private final RewardService rewardService;
    private final UserRepository userRepository;

    public ComplaintService(ComplaintRepository complaintRepository,
                             NotificationService notificationService,
                             RewardService rewardService,
                             UserRepository userRepository) {
        this.complaintRepository = complaintRepository;
        this.notificationService = notificationService;
        this.rewardService = rewardService;
        this.userRepository = userRepository;
    }

    public Complaint submit(Citizen citizen, String title, String description, String category,
                             String division, String district, String upazila) {
        Complaint complaint = new Complaint(title, description, category, division, district, upazila, citizen.getId());
        complaintRepository.save(complaint);
        rewardService.award(citizen, RewardService.POINTS_COMPLAINT);
        notificationService.notify(citizen.getId(),
                "Your complaint " + complaint.getTrackingId() + " has been submitted.",
                Notification.Type.COMPLAINT, "/citizen/complaints/" + complaint.getId());
        return complaint;
    }

    public List<Complaint> all() { return complaintRepository.findAll(); }
    public List<Complaint> forCitizen(Long citizenId) { return complaintRepository.findByCitizenId(citizenId); }
    public List<Complaint> forOfficer(Long officerId) { return complaintRepository.findByAssignedOfficerId(officerId); }
    public Optional<Complaint> find(Long id) { return complaintRepository.findById(id); }

    /** Complaints a citizen has bookmarked/saved, newest first. */
    public List<Complaint> savedBy(Long citizenId) {
        return complaintRepository.findAll().stream()
                .filter(c -> c.getBookmarkedBy().contains(citizenId))
                .collect(Collectors.toList());
    }

    public void toggleBookmark(Complaint complaint, Long citizenId) {
        complaint.toggleBookmark(citizenId);
    }

    public void assign(Complaint complaint, Officer officer) {
        complaint.setAssignedOfficerId(officer.getId());
        complaint.setStatus(ComplaintStatus.ASSIGNED);
        complaint.addTimelineEvent("Assigned to officer",
                "Handed over to " + officer.getFullName() + " (" + officer.getDepartment() + ")", officer.getFullName());
        rewardService.awardOfficer(officer, RewardService.POINTS_OFFICER_ACCEPT);
        notificationService.notify(complaint.getCitizenId(),
                "Your complaint " + complaint.getTrackingId() + " was assigned to an officer.",
                Notification.Type.COMPLAINT, "/citizen/complaints/" + complaint.getId());
    }

    /**
     * Assigns (or reassigns) a complaint to a specific officer chosen from the
     * queue — used both for a still-unassigned complaint ("assign to officer")
     * and an already-assigned one being handed off (e.g. wrong department,
     * overload, escalation). Mirrors {@link #assign} but records both the
     * acting officer and the receiving officer in the timeline, and does not
     * re-award the "accepted" reward point (only the receiving officer's
     * future actions count).
     */
    public void reassign(Complaint complaint, Officer fromOfficer, Officer toOfficer, String reason) {
        boolean wasUnassigned = complaint.getAssignedOfficerId() == null;
        complaint.setAssignedOfficerId(toOfficer.getId());
        if (wasUnassigned && (complaint.getStatus() == ComplaintStatus.SUBMITTED || complaint.getStatus() == ComplaintStatus.PENDING)) {
            complaint.setStatus(ComplaintStatus.ASSIGNED);
        }
        String description = wasUnassigned
                ? "Assigned to " + toOfficer.getFullName() + " (" + toOfficer.getDepartment() + ") by "
                        + fromOfficer.getFullName() + (reason != null && !reason.isBlank() ? " — " + reason : "")
                : "Moved from " + fromOfficer.getFullName() + " (" + fromOfficer.getDepartment() + ") to "
                        + toOfficer.getFullName() + " (" + toOfficer.getDepartment() + ")"
                        + (reason != null && !reason.isBlank() ? " — " + reason : "");
        complaint.addTimelineEvent(wasUnassigned ? "Assigned to officer" : "Reassigned", description, fromOfficer.getFullName());
        notificationService.notify(complaint.getCitizenId(),
                wasUnassigned
                        ? "Your complaint " + complaint.getTrackingId() + " was assigned to an officer."
                        : "Your complaint " + complaint.getTrackingId() + " was reassigned to another officer.",
                Notification.Type.COMPLAINT, "/citizen/complaints/" + complaint.getId());
    }

    public void changeStatus(Complaint complaint, ComplaintStatus status, String note, Officer officer) {
        complaint.setStatus(status);
        complaint.addTimelineEvent(status.getLabel(), note, officer.getFullName());
        if (status == ComplaintStatus.RESOLVED) {
            rewardService.awardOfficer(officer, RewardService.POINTS_OFFICER_RESOLVE);
            userRepository.findById(complaint.getCitizenId()).ifPresent(u -> {
                if (u instanceof Citizen citizen) rewardService.award(citizen, RewardService.POINTS_COMPLAINT_RESOLVED_BONUS);
            });
        }
        notificationService.notify(complaint.getCitizenId(),
                "Complaint " + complaint.getTrackingId() + " is now " + status.getLabel() + ".",
                Notification.Type.COMPLAINT, "/citizen/complaints/" + complaint.getId());
    }

    public void reply(Complaint complaint, String message, Officer officer) {
        complaint.addTimelineEvent("Officer reply", message, officer.getFullName());
        notificationService.notify(complaint.getCitizenId(),
                "New reply on complaint " + complaint.getTrackingId() + ".",
                Notification.Type.COMPLAINT, "/citizen/complaints/" + complaint.getId());
    }

    public void rateResolution(Complaint complaint, int rating, String feedback) {
        complaint.setRating(rating);
        complaint.setFeedback(feedback);
        complaint.addTimelineEvent("Citizen feedback", "Rated " + rating + "/5 — " + feedback, "Citizen");
    }

    public void reopen(Complaint complaint, String reason) {
        complaint.setStatus(ComplaintStatus.UNDER_REVIEW);
        complaint.addTimelineEvent("Complaint reopened", reason, "Citizen");
    }

    // ---------- Comments (with reply threading, likes, @mentions) ----------

    public Comment addComment(Complaint complaint, Long authorId, String content, Long parentId) {
        Comment comment = new Comment(authorId, content, parentId);
        complaint.getComments().add(comment);
        if (!authorId.equals(complaint.getCitizenId())) {
            notificationService.notify(complaint.getCitizenId(),
                    "New comment on complaint " + complaint.getTrackingId() + ".",
                    Notification.Type.COMPLAINT, "/citizen/complaints/" + complaint.getId());
        }
        return comment;
    }

    public void toggleCommentLike(Complaint complaint, Long commentId, Long userId) {
        complaint.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .ifPresent(c -> c.toggleLike(userId));
    }

    public double averageResolutionSatisfaction() {
        return complaintRepository.findAll().stream()
                .filter(c -> c.getRating() != null)
                .mapToInt(Complaint::getRating)
                .average().orElse(0);
    }

    public long total() { return complaintRepository.count(); }
    public long resolved() { return complaintRepository.countResolved(); }
    public long pending() { return complaintRepository.countPending(); }

    /** "Pending" bucket for officer dashboard filter cards: freshly submitted / not yet actioned. */
    public long pendingBucket() {
        return complaintRepository.findAll().stream()
                .filter(c -> c.getStatus() == ComplaintStatus.SUBMITTED || c.getStatus() == ComplaintStatus.PENDING)
                .count();
    }

    /** "Awaiting" bucket: actively being worked on / waiting on a response. */
    public long awaitingBucket() {
        return complaintRepository.findAll().stream()
                .filter(c -> c.getStatus() == ComplaintStatus.ASSIGNED || c.getStatus() == ComplaintStatus.UNDER_REVIEW
                        || c.getStatus() == ComplaintStatus.IN_PROGRESS || c.getStatus() == ComplaintStatus.WAITING_FOR_CITIZEN)
                .count();
    }
}

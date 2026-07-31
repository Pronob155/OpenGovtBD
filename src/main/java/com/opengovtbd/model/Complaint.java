package com.opengovtbd.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Complaint {

    private static final AtomicLong SEQUENCE = new AtomicLong(2000);

    private final Long id;
    private final String trackingId;
    private final Long citizenId;
    private String title;
    private String description;
    private String category;
    private String division;
    private String district;
    private String upazila;
    private final List<String> imageNames = new ArrayList<>();
    private ComplaintStatus status = ComplaintStatus.SUBMITTED;
    private Long assignedOfficerId;
    private Integer rating;
    private String feedback;
    private final LocalDateTime createdAt;
    private final List<TimelineEvent> timeline = new ArrayList<>();
    private final List<Comment> comments = new ArrayList<>();
    private final Set<Long> bookmarkedBy = new HashSet<>();

    public Complaint(String title, String description, String category, String division,
                      String district, String upazila, Long citizenId) {
        this.id = SEQUENCE.incrementAndGet();
        this.trackingId = "NS-" + LocalDateTime.now().getYear() + "-" + this.id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.citizenId = citizenId;
        this.createdAt = LocalDateTime.now();
        this.timeline.add(new TimelineEvent("Complaint submitted",
                "Citizen filed a new complaint under " + category, "Citizen"));
    }

    public void addTimelineEvent(String title, String description, String actor) {
        this.timeline.add(new TimelineEvent(title, description, actor));
    }

    public Long getId() { return id; }
    public String getTrackingId() { return trackingId; }
    public Long getCitizenId() { return citizenId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getDivision() { return division; }
    public String getDistrict() { return district; }
    public String getUpazila() { return upazila; }
    public List<String> getImageNames() { return imageNames; }
    public ComplaintStatus getStatus() { return status; }
    public void setStatus(ComplaintStatus status) { this.status = status; }
    public Long getAssignedOfficerId() { return assignedOfficerId; }
    public void setAssignedOfficerId(Long assignedOfficerId) { this.assignedOfficerId = assignedOfficerId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<TimelineEvent> getTimeline() { return timeline; }
    public List<Comment> getComments() { return comments; }
    public int getCommentCount() { return comments.size(); }
    public Set<Long> getBookmarkedBy() { return bookmarkedBy; }
    public void toggleBookmark(Long userId) {
        if (!bookmarkedBy.remove(userId)) bookmarkedBy.add(userId);
    }

    public boolean isResolved() {
        return status == ComplaintStatus.RESOLVED || status == ComplaintStatus.CLOSED;
    }

    /** Human friendly relative timestamp, e.g. "5m ago", "3h ago". */
    public String getRelativeTime() {
        return TimeFormat.relative(createdAt);
    }
}

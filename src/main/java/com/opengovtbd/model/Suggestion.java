package com.opengovtbd.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Suggestion {
    private static final AtomicLong SEQUENCE = new AtomicLong(6000);

    private final Long id;
    private final Long authorId;
    private String title;
    private String description;
    private SuggestionStatus status = SuggestionStatus.SUBMITTED;
    private final Set<Long> upvotedBy = new HashSet<>();
    private final Set<Long> downvotedBy = new HashSet<>();
    private final List<Comment> comments = new ArrayList<>();
    private final List<TimelineEvent> timeline = new ArrayList<>();
    private String governmentFeedback;
    private final LocalDateTime createdAt;

    public Suggestion(Long authorId, String title, String description) {
        this.id = SEQUENCE.incrementAndGet();
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.timeline.add(new TimelineEvent("Suggestion submitted", "Awaiting review", "Citizen"));
    }

    public Long getId() { return id; }
    public Long getAuthorId() { return authorId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public SuggestionStatus getStatus() { return status; }
    public void setStatus(SuggestionStatus status) { this.status = status; }
    public Set<Long> getUpvotedBy() { return upvotedBy; }
    public Set<Long> getDownvotedBy() { return downvotedBy; }
    public List<Comment> getComments() { return comments; }
    public List<TimelineEvent> getTimeline() { return timeline; }
    public String getGovernmentFeedback() { return governmentFeedback; }
    public void setGovernmentFeedback(String governmentFeedback) { this.governmentFeedback = governmentFeedback; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public int getScore() { return upvotedBy.size() - downvotedBy.size(); }
}

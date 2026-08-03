package com.opengovtbd.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Notification {
    private static final AtomicLong SEQUENCE = new AtomicLong(7000);

    public enum Type { COMPLAINT, NOTICE, POLL, DISCUSSION, SUGGESTION, MAINTENANCE, EMERGENCY, SECURITY }

    private final Long id;
    private final Long userId;
    private String message;
    private Type type;
    private boolean read = false;
    private final LocalDateTime createdAt;
    private String link;

    public Notification(Long userId, String message, Type type, String link) {
        this.id = SEQUENCE.incrementAndGet();
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.link = link;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getMessage() { return message; }
    public Type getType() { return type; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getLink() { return link; }

    public String getRelativeTime() {
        return TimeFormat.relative(createdAt);
    }

    public String getBucket() {
        long hoursAgo = java.time.Duration.between(createdAt, LocalDateTime.now()).toHours();
        if (hoursAgo < 24) return "today";
        if (hoursAgo < 24 * 7) return "week";
        return "earlier";
    }

    public String getTitle() {
        switch (type) {
            case COMPLAINT: return "Complaint Update";
            case POLL: return "Poll Update";
            case DISCUSSION: return "New Discussion Activity";
            case SUGGESTION: return "Suggestion Update";
            case MAINTENANCE: return "Maintenance Notice";
            case EMERGENCY: return "Emergency Alert";
            case SECURITY: return "Security Update";
            default: return "Notice";
        }
    }

    public String getIconName() {
        switch (type) {
            case COMPLAINT: return "report";
            case POLL: return "how_to_vote";
            case DISCUSSION: return "forum";
            case SUGGESTION: return "lightbulb";
            case MAINTENANCE: return "build";
            case EMERGENCY: return "emergency";
            case SECURITY: return "verified_user";
            default: return "notifications";
        }
    }

    public String getIconClass() {
        switch (type) {
            case COMPLAINT: return "notif-icon-complaint";
            case EMERGENCY: return "notif-icon-emergency";
            case DISCUSSION:
            case SUGGESTION:
            case POLL: return "notif-icon-community";
            default: return "notif-icon-notice";
        }
    }
}

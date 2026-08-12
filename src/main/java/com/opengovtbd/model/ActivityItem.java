package com.opengovtbd.model;

import java.time.LocalDateTime;

/**
 * A single row in a citizen's "My Activity" timeline — a read-only aggregation
 * over their own complaints, discussions, and suggestions, including content
 * that isn't visible anywhere else yet (e.g. a discussion still awaiting
 * officer approval).
 */
public class ActivityItem {

    private final String type; // "Complaint" | "Discussion" | "Suggestion"
    private final String icon; // Material Symbols icon name
    private final String title;
    private final String statusLabel;
    private final String statusTone; // matches the pill tone convention: info/success/warning/error/muted
    private final LocalDateTime timestamp;
    private final String link;

    public ActivityItem(String type, String icon, String title, String statusLabel,
            String statusTone, LocalDateTime timestamp, String link) {
        this.type = type;
        this.icon = icon;
        this.title = title;
        this.statusLabel = statusLabel;
        this.statusTone = statusTone;
        this.timestamp = timestamp;
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public String getStatusTone() {
        return statusTone;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getLink() {
        return link;
    }
}

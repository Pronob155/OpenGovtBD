package com.opengovtbd.model;

import java.time.LocalDateTime;

/** A single step in a complaint's or suggestion's audit trail. */
public class TimelineEvent {
    private final String title;
    private final String description;
    private final LocalDateTime timestamp;
    private final String actor;

    public TimelineEvent(String title, String description, String actor) {
        this.title = title;
        this.description = description;
        this.actor = actor;
        this.timestamp = LocalDateTime.now();
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getActor() { return actor; }
}

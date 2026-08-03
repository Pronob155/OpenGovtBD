package com.opengovtbd.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Announcement {
    private static final AtomicLong SEQUENCE = new AtomicLong(8000);

    public enum Priority { NORMAL, IMPORTANT, EMERGENCY }

    private final Long id;
    private String title;
    private String content;
    private Priority priority;
    private final LocalDateTime createdAt;

    public Announcement(String title, String content, Priority priority) {
        this.id = SEQUENCE.incrementAndGet();
        this.title = title;
        this.content = content;
        this.priority = priority;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Priority getPriority() { return priority; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

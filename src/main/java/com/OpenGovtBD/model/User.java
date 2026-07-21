package com.OenGovtBD.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public abstract class User {

    private static final AtomicLong SEQUENCE = new AtomicLong(1000);

    private final Long id;
    private String fullName;
    private String password; 
    private final LocalDateTime createdAt;
    private boolean active = true;

    protected User(String fullName, String password) {
        this.id = SEQUENCE.incrementAndGet();
        this.fullName = fullName;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    public abstract String getDashboardUrl();

    public abstract String getDisplayRole();

    public abstract Role getRole();

    public abstract String getLoginIdentifier();

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getInitials() {
        if (fullName == null || fullName.isBlank())
            return "U";
        String[] parts = fullName.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(2, parts.length); i++) {
            sb.append(Character.toUpperCase(parts[i].charAt(0)));
        }
        return sb.toString();
    }
}



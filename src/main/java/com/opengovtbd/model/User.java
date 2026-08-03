package com.opengovtbd.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Abstract base type for every account on the platform.
 * Citizen, Officer and Admin all extend this class, demonstrating
 * inheritance and polymorphism (see {@link #getDashboardUrl()} and
 * {@link #getDisplayRole()}).
 */
public abstract class User {

    private static final AtomicLong SEQUENCE = new AtomicLong(1000);

    private final Long id;
    private String fullName;
    private String username;
    private String password; // demo-only plain storage, never do this in production
    private final LocalDateTime createdAt;
    private boolean active = true;

    // ---- Suspension / ban (admin moderation) ----
    private boolean banned = false;
    private LocalDate suspendedUntil;
    private int suspensionCount = 0;
    private final List<SuspensionRecord> suspensionHistory = new ArrayList<>();

    protected User(String fullName, String password) {
        this.id = SEQUENCE.incrementAndGet();
        this.fullName = fullName;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.username = slugify(fullName) + "-" + this.id;
    }

    private static String slugify(String name) {
        if (name == null || name.isBlank()) return "user";
        return name.trim().toLowerCase()
                .replaceAll("[^a-z0-9]+", ".")
                .replaceAll("(^\\.|\\.$)", "");
    }

    /** Where this user lands right after login. */
    public abstract String getDashboardUrl();

    /** Human readable role label used across the UI. */
    public abstract String getDisplayRole();

    public abstract Role getRole();

    /** The value this user authenticates with (phone, officer ID, or email). */
    public abstract String getLoginIdentifier();

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    // ---- Suspension / ban ----
    public boolean isBanned() { return banned; }
    public LocalDate getSuspendedUntil() { return suspendedUntil; }
    public int getSuspensionCount() { return suspensionCount; }
    public List<SuspensionRecord> getSuspensionHistory() { return suspensionHistory; }

    public boolean isCurrentlySuspended() {
        return !banned && suspendedUntil != null && !suspendedUntil.isBefore(LocalDate.now());
    }

    /** True when the account is blocked from logging in (banned or actively suspended). */
    public boolean isLoginBlocked() { return banned || isCurrentlySuspended(); }

    /**
     * Applies a temporary suspension. On the 5th suspension the account is
     * automatically converted into a permanent ban, per platform policy.
     */
    public void suspend(LocalDate until, String reason, String issuedBy) {
        this.suspensionCount++;
        this.suspensionHistory.add(new SuspensionRecord(SuspensionRecord.Type.SUSPENSION, reason, until, issuedBy));
        if (this.suspensionCount >= 5) {
            ban("Automatically banned after " + this.suspensionCount + " suspensions.", issuedBy);
            return;
        }
        this.suspendedUntil = until;
        this.active = false;
    }

    public void ban(String reason, String issuedBy) {
        this.banned = true;
        this.suspendedUntil = null;
        this.active = false;
        this.suspensionHistory.add(new SuspensionRecord(SuspensionRecord.Type.BAN, reason, null, issuedBy));
    }

    /** Lifts any active suspension/ban and reactivates the account. */
    public void reinstate() {
        this.banned = false;
        this.suspendedUntil = null;
        this.active = true;
    }

    public String getInitials() {
        if (fullName == null || fullName.isBlank()) return "U";
        String[] parts = fullName.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(2, parts.length); i++) {
            sb.append(Character.toUpperCase(parts[i].charAt(0)));
        }
        return sb.toString();
    }
}

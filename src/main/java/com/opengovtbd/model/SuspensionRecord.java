package com.opengovtbd.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class SuspensionRecord {

    private static final AtomicLong SEQUENCE = new AtomicLong(9000);

    public enum Type { SUSPENSION, BAN }

    private final Long id;
    private final Type type;
    private final String reason;
    private final LocalDate until; // null for permanent bans
    private final String issuedBy;
    private final LocalDateTime issuedAt;

    public SuspensionRecord(Type type, String reason, LocalDate until, String issuedBy) {
        this.id = SEQUENCE.incrementAndGet();
        this.type = type;
        this.reason = reason;
        this.until = until;
        this.issuedBy = issuedBy;
        this.issuedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Type getType() { return type; }
    public String getReason() { return reason; }
    public LocalDate getUntil() { return until; }
    public String getIssuedBy() { return issuedBy; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
}

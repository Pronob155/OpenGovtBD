package com.opengovtbd.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class GovService {
    private static final AtomicLong SEQUENCE = new AtomicLong(8000);

    private final Long id;
    private String name;
    private String description;
    private String category;
    private String logoIcon;
    private String externalUrl;
    private final Long createdByOfficerId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GovService(String name, String description, String category, String logoIcon,
            String externalUrl, Long createdByOfficerId) {
        this.id = SEQUENCE.incrementAndGet();
        this.name = name;
        this.description = description;
        this.category = category;
        this.logoIcon = (logoIcon == null || logoIcon.isBlank()) ? "apps" : logoIcon;
        this.externalUrl = externalUrl;
        this.createdByOfficerId = createdByOfficerId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLogoIcon() {
        return logoIcon;
    }

    public void setLogoIcon(String logoIcon) {
        this.logoIcon = logoIcon;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public Long getCreatedByOfficerId() {
        return createdByOfficerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}

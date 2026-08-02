package com.opengovtbd.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Comment {
    private static final AtomicLong SEQUENCE = new AtomicLong(5000);

    private final Long id;
    private final Long authorId;
    private String content;
    private final LocalDateTime createdAt;
    private Long parentId;
    private final Set<Long> likedBy = new HashSet<>();

    public Comment(Long authorId, String content) {
        this.id = SEQUENCE.incrementAndGet();
        this.authorId = authorId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Comment(Long authorId, String content, Long parentId) {
        this(authorId, content);
        this.parentId = parentId;
    }

    public Long getId() { return id; }
    public Long getAuthorId() { return authorId; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getParentId() { return parentId; }
    public boolean isReply() { return parentId != null; }
    public Set<Long> getLikedBy() { return likedBy; }
    public int getLikeCount() { return likedBy.size(); }

    public void toggleLike(Long userId) {
        if (!likedBy.remove(userId)) likedBy.add(userId);
    }

    public String getRelativeTime() {
        return TimeFormat.relative(createdAt);
    }
}

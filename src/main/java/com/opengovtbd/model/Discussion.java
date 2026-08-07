package com.opengovtbd.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Discussion {
    private static final AtomicLong SEQUENCE = new AtomicLong(3000);

    private final Long id;
    private final Long authorId;
    private String title;
    private String content;
    private String category;
    private boolean approved = false;
    private boolean pinned = false;
    private boolean locked = false;
    private String officialResponse;
    private final Set<Long> likedBy = new HashSet<>();
    private final Set<Long> dislikedBy = new HashSet<>();
    private final Set<Long> bookmarkedBy = new HashSet<>();
    private final List<Comment> comments = new ArrayList<>();
    private final LocalDateTime createdAt;

    public Discussion(Long authorId, String title, String content, String category) {
        this.id = SEQUENCE.incrementAndGet();
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getAuthorId() { return authorId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public boolean isPinned() { return pinned; }
    public void setPinned(boolean pinned) { this.pinned = pinned; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public String getOfficialResponse() { return officialResponse; }
    public void setOfficialResponse(String officialResponse) { this.officialResponse = officialResponse; }
    public Set<Long> getLikedBy() { return likedBy; }
    public Set<Long> getDislikedBy() { return dislikedBy; }
    public Set<Long> getBookmarkedBy() { return bookmarkedBy; }
    public List<Comment> getComments() { return comments; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public int getLikeCount() { return likedBy.size(); }
    public int getDislikeCount() { return dislikedBy.size(); }
    public int getCommentCount() { return comments.size(); }
}
package com.opengovtbd.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Discussion {
    private static final AtomicLong SEQUENCE = new AtomicLong(3000);

    private final Long id;
    private final Long authorId;
    private String title;
    private String content;
    private String category;
    private boolean approved = false;
    private boolean pinned = false;
    private boolean locked = false;
    private String officialResponse;
    private final Set<Long> likedBy = new HashSet<>();
    private final Set<Long> dislikedBy = new HashSet<>();
    private final Set<Long> bookmarkedBy = new HashSet<>();
    private final List<Comment> comments = new ArrayList<>();
    private final LocalDateTime createdAt;

    public Discussion(Long authorId, String title, String content, String category) {
        this.id = SEQUENCE.incrementAndGet();
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getAuthorId() { return authorId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public boolean isPinned() { return pinned; }
    public void setPinned(boolean pinned) { this.pinned = pinned; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public String getOfficialResponse() { return officialResponse; }
    public void setOfficialResponse(String officialResponse) { this.officialResponse = officialResponse; }
    public Set<Long> getLikedBy() { return likedBy; }
    public Set<Long> getDislikedBy() { return dislikedBy; }
    public Set<Long> getBookmarkedBy() { return bookmarkedBy; }
    public List<Comment> getComments() { return comments; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public int getLikeCount() { return likedBy.size(); }
    public int getDislikeCount() { return dislikedBy.size(); }
    public int getCommentCount() { return comments.size(); }
}
package com.opengovtbd.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Discussion {
    private static final AtomicLong SEQUENCE = new AtomicLong(3000);

    private final Long id;
    private final Long authorId;
    private String title;
    private String content;
    private String category;
    private boolean approved = false;
    private boolean pinned = false;
    private boolean locked = false;
    private String officialResponse;
    private final Set<Long> likedBy = new HashSet<>();
    private final Set<Long> dislikedBy = new HashSet<>();
    private final Set<Long> bookmarkedBy = new HashSet<>();
    private final List<Comment> comments = new ArrayList<>();
    private final LocalDateTime createdAt;

    public Discussion(Long authorId, String title, String content, String category) {
        this.id = SEQUENCE.incrementAndGet();
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getAuthorId() { return authorId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public boolean isPinned() { return pinned; }
    public void setPinned(boolean pinned) { this.pinned = pinned; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public String getOfficialResponse() { return officialResponse; }
    public void setOfficialResponse(String officialResponse) { this.officialResponse = officialResponse; }
    public Set<Long> getLikedBy() { return likedBy; }
    public Set<Long> getDislikedBy() { return dislikedBy; }
    public Set<Long> getBookmarkedBy() { return bookmarkedBy; }
    public List<Comment> getComments() { return comments; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public int getLikeCount() { return likedBy.size(); }
    public int getDislikeCount() { return dislikedBy.size(); }
    public int getCommentCount() { return comments.size(); }
}
package com.opengovtbd.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Discussion {
    private static final AtomicLong SEQUENCE = new AtomicLong(3000);

    private final Long id;
    private final Long authorId;
    private String title;
    private String content;
    private String category;
    private boolean approved = false;
    private boolean pinned = false;
    private boolean locked = false;
    private String officialResponse;
    private final Set<Long> likedBy = new HashSet<>();
    private final Set<Long> dislikedBy = new HashSet<>();
    private final Set<Long> bookmarkedBy = new HashSet<>();
    private final List<Comment> comments = new ArrayList<>();
    private final LocalDateTime createdAt;

    public Discussion(Long authorId, String title, String content, String category) {
        this.id = SEQUENCE.incrementAndGet();
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getAuthorId() { return authorId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public boolean isPinned() { return pinned; }
    public void setPinned(boolean pinned) { this.pinned = pinned; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public String getOfficialResponse() { return officialResponse; }
    public void setOfficialResponse(String officialResponse) { this.officialResponse = officialResponse; }
    public Set<Long> getLikedBy() { return likedBy; }
    public Set<Long> getDislikedBy() { return dislikedBy; }
    public Set<Long> getBookmarkedBy() { return bookmarkedBy; }
    public List<Comment> getComments() { return comments; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public int getLikeCount() { return likedBy.size(); }
    public int getDislikeCount() { return dislikedBy.size(); }
    public int getCommentCount() { return comments.size(); }
}
package com.opengovtbd.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Discussion {
    private static final AtomicLong SEQUENCE = new AtomicLong(3000);

    private final Long id;
    private final Long authorId;
    private String title;
    private String content;
    private String category;
    private boolean approved = false;
    private boolean pinned = false;
    private boolean locked = false;
    private String officialResponse;
    private final Set<Long> likedBy = new HashSet<>();
    private final Set<Long> dislikedBy = new HashSet<>();
    private final Set<Long> bookmarkedBy = new HashSet<>();
    private final List<Comment> comments = new ArrayList<>();
    private final LocalDateTime createdAt;

    public Discussion(Long authorId, String title, String content, String category) {
        this.id = SEQUENCE.incrementAndGet();
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getAuthorId() { return authorId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public boolean isPinned() { return pinned; }
    public void setPinned(boolean pinned) { this.pinned = pinned; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public String getOfficialResponse() { return officialResponse; }
    public void setOfficialResponse(String officialResponse) { this.officialResponse = officialResponse; }
    public Set<Long> getLikedBy() { return likedBy; }
    public Set<Long> getDislikedBy() { return dislikedBy; }
    public Set<Long> getBookmarkedBy() { return bookmarkedBy; }
    public List<Comment> getComments() { return comments; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public int getLikeCount() { return likedBy.size(); }
    public int getDislikeCount() { return dislikedBy.size(); }
    public int getCommentCount() { return comments.size(); }
}
package com.opengovtbd.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Discussion {
    private static final AtomicLong SEQUENCE = new AtomicLong(3000);

    private final Long id;
    private final Long authorId;
    private String title;
    private String content;
    private String category;
    private boolean approved = false;
    private boolean pinned = false;
    private boolean locked = false;
    private String officialResponse;
    private final Set<Long> likedBy = new HashSet<>();
    private final Set<Long> dislikedBy = new HashSet<>();
    private final Set<Long> bookmarkedBy = new HashSet<>();
    private final List<Comment> comments = new ArrayList<>();
    private final LocalDateTime createdAt;

    public Discussion(Long authorId, String title, String content, String category) {
        this.id = SEQUENCE.incrementAndGet();
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getAuthorId() { return authorId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public boolean isPinned() { return pinned; }
    public void setPinned(boolean pinned) { this.pinned = pinned; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public String getOfficialResponse() { return officialResponse; }
    public void setOfficialResponse(String officialResponse) { this.officialResponse = officialResponse; }
    public Set<Long> getLikedBy() { return likedBy; }
    public Set<Long> getDislikedBy() { return dislikedBy; }
    public Set<Long> getBookmarkedBy() { return bookmarkedBy; }
    public List<Comment> getComments() { return comments; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public int getLikeCount() { return likedBy.size(); }
    public int getDislikeCount() { return dislikedBy.size(); }
    public int getCommentCount() { return comments.size(); }
}

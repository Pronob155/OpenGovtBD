package com.opengovtbd.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/** An official poll created by the government for citizen participation. */
public class Poll {
    private static final AtomicLong SEQUENCE = new AtomicLong(4000);

    private final Long id;
    private String question;
    private String category;
    private final List<PollOption> options = new ArrayList<>();
    private final Set<Long> votedCitizens = new HashSet<>();
    private final Set<Long> bookmarkedBy = new HashSet<>();
    private final LocalDateTime createdAt;
    private LocalDateTime deadline;
    private boolean anonymous = true;

    public Poll(String question, String category, List<String> optionTexts, LocalDateTime deadline) {
        this.id = SEQUENCE.incrementAndGet();
        this.question = question;
        this.category = category;
        this.deadline = deadline;
        this.createdAt = LocalDateTime.now();
        for (String t : optionTexts) this.options.add(new PollOption(t));
    }

    public boolean isActive() { return deadline == null || LocalDateTime.now().isBefore(deadline); }

    public int getTotalVotes() {
        return options.stream().mapToInt(PollOption::getVotes).sum();
    }

    public double percentageFor(PollOption option) {
        int total = getTotalVotes();
        if (total == 0) return 0;
        return (option.getVotes() * 100.0) / total;
    }

    public Long getId() { return id; }
    public String getQuestion() { return question; }
    public String getCategory() { return category; }
    public List<PollOption> getOptions() { return options; }
    public Set<Long> getVotedCitizens() { return votedCitizens; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getDeadline() { return deadline; }
    public boolean isAnonymous() { return anonymous; }
    public Set<Long> getBookmarkedBy() { return bookmarkedBy; }
    public void toggleBookmark(Long userId) {
        if (!bookmarkedBy.remove(userId)) bookmarkedBy.add(userId);
    }

    /** Human friendly relative timestamp, e.g. "5m ago", "3h ago". */
    public String getRelativeTime() {
        return TimeFormat.relative(createdAt);
    }
}

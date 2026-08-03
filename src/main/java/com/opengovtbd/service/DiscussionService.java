package com.opengovtbd.service;

import com.opengovtbd.model.*;
import com.opengovtbd.repository.DiscussionRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final NotificationService notificationService;
    private final RewardService rewardService;

    public DiscussionService(DiscussionRepository discussionRepository,
                              NotificationService notificationService,
                              RewardService rewardService) {
        this.discussionRepository = discussionRepository;
        this.notificationService = notificationService;
        this.rewardService = rewardService;
    }

    public Discussion create(Citizen citizen, String title, String content, String category) {
        Discussion discussion = new Discussion(citizen.getId(), title, content, category);
        discussionRepository.save(discussion);
        rewardService.award(citizen, RewardService.POINTS_DISCUSSION, "Started a discussion");
        return discussion;
    }

    public List<Discussion> savedBy(Long citizenId) {
        return discussionRepository.findAll().stream()
                .filter(d -> d.getBookmarkedBy().contains(citizenId))
                .collect(Collectors.toList());
    }

    public List<Discussion> approvedFeed(String sort, String category, String query) {
        List<Discussion> list = discussionRepository.findApproved();
        if (category != null && !category.isBlank() && !category.equalsIgnoreCase("all")) {
            list = list.stream().filter(d -> d.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());
        }
        if (query != null && !query.isBlank()) {
            String q = query.toLowerCase();
            list = list.stream().filter(d -> d.getTitle().toLowerCase().contains(q)
                    || d.getContent().toLowerCase().contains(q)).collect(Collectors.toList());
        }
        if ("liked".equalsIgnoreCase(sort)) {
            list = list.stream().sorted(Comparator.comparingInt(Discussion::getLikeCount).reversed()).collect(Collectors.toList());
        } else if ("trending".equalsIgnoreCase(sort)) {
            list = list.stream()
                    .sorted(Comparator.comparingInt((Discussion d) -> d.getLikeCount() + d.getCommentCount() * 2).reversed())
                    .collect(Collectors.toList());
        }
        return list;
    }

    public List<Discussion> pendingApproval() { return discussionRepository.findPendingApproval(); }
    public Optional<Discussion> find(Long id) { return discussionRepository.findById(id); }

    public void approve(Discussion discussion) {
        discussion.setApproved(true);
        notificationService.notify(discussion.getAuthorId(),
                "Your discussion \"" + discussion.getTitle() + "\" was approved and is now public.",
                Notification.Type.DISCUSSION, "/citizen/discussions/" + discussion.getId());
    }

    public void reject(Discussion discussion) {
        notificationService.notify(discussion.getAuthorId(),
                "Your discussion \"" + discussion.getTitle() + "\" was not approved by moderators.",
                Notification.Type.DISCUSSION, "/citizen/discussions");
    }

    public void toggleLike(Discussion discussion, Long userId) {
        discussion.getDislikedBy().remove(userId);
        if (!discussion.getLikedBy().remove(userId)) discussion.getLikedBy().add(userId);
    }

    public void toggleDislike(Discussion discussion, Long userId) {
        discussion.getLikedBy().remove(userId);
        if (!discussion.getDislikedBy().remove(userId)) discussion.getDislikedBy().add(userId);
    }

    public void toggleBookmark(Discussion discussion, Long userId) {
        if (!discussion.getBookmarkedBy().remove(userId)) discussion.getBookmarkedBy().add(userId);
    }

    public void addComment(Discussion discussion, Long authorId, String content, Long parentId) {
        if (discussion.isLocked()) throw new IllegalStateException("Comments are locked on this discussion.");
        discussion.getComments().add(new Comment(authorId, content, parentId));
        if (!authorId.equals(discussion.getAuthorId())) {
            notificationService.notify(discussion.getAuthorId(),
                    "New comment on your discussion \"" + discussion.getTitle() + "\".",
                    Notification.Type.DISCUSSION, "/citizen/discussions/" + discussion.getId());
        }
    }

    public void toggleCommentLike(Discussion discussion, Long commentId, Long userId) {
        discussion.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .ifPresent(c -> c.toggleLike(userId));
    }

    public void setOfficialResponse(Discussion discussion, String response) {
        discussion.setOfficialResponse(response);
    }

    public void togglePin(Discussion discussion) { discussion.setPinned(!discussion.isPinned()); }
    public void toggleLock(Discussion discussion) { discussion.setLocked(!discussion.isLocked()); }
}

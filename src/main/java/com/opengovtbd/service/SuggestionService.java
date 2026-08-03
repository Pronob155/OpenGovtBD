package com.opengovtbd.service;

import com.opengovtbd.model.*;
import com.opengovtbd.repository.SuggestionRepository;
import com.opengovtbd.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final NotificationService notificationService;
    private final RewardService rewardService;
    private final UserRepository userRepository;

    public SuggestionService(SuggestionRepository suggestionRepository,
                              NotificationService notificationService,
                              RewardService rewardService,
                              UserRepository userRepository) {
        this.suggestionRepository = suggestionRepository;
        this.notificationService = notificationService;
        this.rewardService = rewardService;
        this.userRepository = userRepository;
    }

    public Suggestion submit(Citizen citizen, String title, String description) {
        Suggestion suggestion = new Suggestion(citizen.getId(), title, description);
        suggestionRepository.save(suggestion);
        rewardService.award(citizen, RewardService.POINTS_SUGGESTION);
        return suggestion;
    }

    public List<Suggestion> all() { return suggestionRepository.findAll(); }
    public List<Suggestion> forCitizen(Long citizenId) { return suggestionRepository.findByAuthorId(citizenId); }
    public Optional<Suggestion> find(Long id) { return suggestionRepository.findById(id); }

    public void upvote(Suggestion suggestion, Long userId) {
        suggestion.getDownvotedBy().remove(userId);
        if (!suggestion.getUpvotedBy().remove(userId)) suggestion.getUpvotedBy().add(userId);
    }

    public void downvote(Suggestion suggestion, Long userId) {
        suggestion.getUpvotedBy().remove(userId);
        if (!suggestion.getDownvotedBy().remove(userId)) suggestion.getDownvotedBy().add(userId);
    }

    public void updateStatus(Suggestion suggestion, SuggestionStatus status, String feedback) {
        suggestion.setStatus(status);
        suggestion.setGovernmentFeedback(feedback);
        suggestion.getTimeline().add(new TimelineEvent(status.getLabel(), feedback, "Government Officer"));
        if (status == SuggestionStatus.IMPLEMENTED) {
            userRepository.findById(suggestion.getAuthorId()).ifPresent(u -> {
                if (u instanceof Citizen citizen) {
                    rewardService.award(citizen, RewardService.POINTS_COMMUNITY_CONTRIBUTION, "Community contribution implemented");
                }
            });
        }
        notificationService.notify(suggestion.getAuthorId(),
                "Your suggestion \"" + suggestion.getTitle() + "\" is now " + status.getLabel() + ".",
                Notification.Type.SUGGESTION, "/citizen/suggestions/" + suggestion.getId());
    }

    public void addComment(Suggestion suggestion, Long authorId, String content) {
        suggestion.getComments().add(new Comment(authorId, content));
    }
}

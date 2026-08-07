package com.opengovtbd.service;

import com.opengovtbd.model.Complaint;
import com.opengovtbd.repository.ComplaintRepository;
import com.opengovtbd.repository.DiscussionRepository;
import com.opengovtbd.repository.PollRepository;
import com.opengovtbd.repository.SuggestionRepository;
import com.opengovtbd.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Aggregates cross-cutting statistics for the analytics dashboard. */
@Service
public class AnalyticsService {

    private final UserRepository userRepository;
    private final ComplaintRepository complaintRepository;
    private final DiscussionRepository discussionRepository;
    private final PollRepository pollRepository;
    private final SuggestionRepository suggestionRepository;

    public AnalyticsService(UserRepository userRepository, ComplaintRepository complaintRepository,
                             DiscussionRepository discussionRepository, PollRepository pollRepository,
                             SuggestionRepository suggestionRepository) {
        this.userRepository = userRepository;
        this.complaintRepository = complaintRepository;
        this.discussionRepository = discussionRepository;
        this.pollRepository = pollRepository;
        this.suggestionRepository = suggestionRepository;
    }

    public long totalCitizens() { return userRepository.countCitizens(); }
    public long verifiedCitizens() { return userRepository.countVerifiedCitizens(); }
    public long totalComplaints() { return complaintRepository.count(); }
    public long resolvedComplaints() { return complaintRepository.countResolved(); }
    public long pendingComplaints() { return complaintRepository.countPending(); }
    public long totalDiscussions() { return discussionRepository.findAll().size(); }
    public long totalPolls() { return pollRepository.findAll().size(); }
    public long totalSuggestions() { return suggestionRepository.findAll().size(); }

    public double resolutionRate() {
        long total = totalComplaints();
        return total == 0 ? 0 : (resolvedComplaints() * 100.0) / total;
    }

    /** Complaint volume grouped by category, for the "most requested / reported" chart. */
    public Map<String, Long> complaintsByCategory() {
        Map<String, Long> map = complaintRepository.findAll().stream()
                .collect(Collectors.groupingBy(Complaint::getCategory, LinkedHashMap::new, Collectors.counting()));
        return map;
    }

    public Map<String, Long> complaintsByDivision() {
        return complaintRepository.findAll().stream()
                .collect(Collectors.groupingBy(Complaint::getDivision, LinkedHashMap::new, Collectors.counting()));
    }

    public List<Complaint> recentComplaints(int limit) {
        return complaintRepository.findAll().stream().limit(limit).collect(Collectors.toList());
    }
}

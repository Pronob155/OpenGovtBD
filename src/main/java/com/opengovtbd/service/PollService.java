package com.opengovtbd.service;

import com.opengovtbd.model.Citizen;
import com.opengovtbd.model.Poll;
import com.opengovtbd.model.PollOption;
import com.opengovtbd.repository.PollRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PollService {

    private final PollRepository pollRepository;
    private final RewardService rewardService;

    public PollService(PollRepository pollRepository, RewardService rewardService) {
        this.pollRepository = pollRepository;
        this.rewardService = rewardService;
    }

    public Poll create(String question, String category, List<String> options, LocalDateTime deadline) {
        Poll poll = new Poll(question, category, options, deadline);
        return pollRepository.save(poll);
    }

    public List<Poll> active() { return pollRepository.findActive(); }
    public List<Poll> archived() { return pollRepository.findArchived(); }
    public Optional<Poll> find(Long id) { return pollRepository.findById(id); }

    public void vote(Poll poll, PollOption option, Citizen citizen) {
        if (!poll.isActive()) throw new IllegalStateException("This poll has closed.");
        if (poll.getVotedCitizens().contains(citizen.getId())) {
            throw new IllegalStateException("You have already voted in this poll.");
        }
        option.incrementVotes();
        poll.getVotedCitizens().add(citizen.getId());
        rewardService.award(citizen, RewardService.POINTS_VOTE, "Voted in a poll");
    }

    public void toggleBookmark(Poll poll, Long citizenId) {
        poll.toggleBookmark(citizenId);
    }

    public List<Poll> savedBy(Long citizenId) {
        return pollRepository.findAll().stream().filter(p -> p.getBookmarkedBy().contains(citizenId)).toList();
    }
}


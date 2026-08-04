package com.opengovtbd.repository;

import com.opengovtbd.model.Poll;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class PollRepository {

    private final ConcurrentHashMap<Long, Poll> polls = new ConcurrentHashMap<>();

    public Poll save(Poll poll) {
        polls.put(poll.getId(), poll);
        return poll;
    }

    public Optional<Poll> findById(Long id) {
        return Optional.ofNullable(polls.get(id));
    }

    public List<Poll> findAll() {
        return polls.values().stream()
                .sorted(Comparator.comparing(Poll::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public List<Poll> findActive() {
        return findAll().stream().filter(Poll::isActive).collect(Collectors.toList());
    }

    public List<Poll> findArchived() {
        return findAll().stream().filter(p -> !p.isActive()).collect(Collectors.toList());
    }
}

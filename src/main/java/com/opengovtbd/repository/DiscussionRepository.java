package com.opengovtbd.repository;

import com.opengovtbd.model.Discussion;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class DiscussionRepository {

    private final ConcurrentHashMap<Long, Discussion> discussions = new ConcurrentHashMap<>();

    public Discussion save(Discussion discussion) {
        discussions.put(discussion.getId(), discussion);
        return discussion;
    }

    public Optional<Discussion> findById(Long id) {
        return Optional.ofNullable(discussions.get(id));
    }

    public List<Discussion> findAll() {
        return discussions.values().stream()
                .sorted(Comparator.comparing(Discussion::isPinned).reversed()
                        .thenComparing(Discussion::getCreatedAt, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public List<Discussion> findApproved() {
        return findAll().stream().filter(Discussion::isApproved).collect(Collectors.toList());
    }

    public List<Discussion> findPendingApproval() {
        return findAll().stream().filter(d -> !d.isApproved()).collect(Collectors.toList());
    }
}

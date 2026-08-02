package com.opengovtbd.repository;

import com.opengovtbd.model.Suggestion;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class SuggestionRepository {

    private final ConcurrentHashMap<Long, Suggestion> suggestions = new ConcurrentHashMap<>();

    public Suggestion save(Suggestion suggestion) {
        suggestions.put(suggestion.getId(), suggestion);
        return suggestion;
    }

    public Optional<Suggestion> findById(Long id) {
        return Optional.ofNullable(suggestions.get(id));
    }

    public List<Suggestion> findAll() {
        return suggestions.values().stream()
                .sorted(Comparator.comparing(Suggestion::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public List<Suggestion> findByAuthorId(Long authorId) {
        return findAll().stream().filter(s -> s.getAuthorId().equals(authorId)).collect(Collectors.toList());
    }
}

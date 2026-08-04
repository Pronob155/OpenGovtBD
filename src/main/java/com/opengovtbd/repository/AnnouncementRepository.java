package com.opengovtbd.repository;

import com.opengovtbd.model.Announcement;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class AnnouncementRepository {

    private final ConcurrentHashMap<Long, Announcement> announcements = new ConcurrentHashMap<>();

    public Announcement save(Announcement announcement) {
        announcements.put(announcement.getId(), announcement);
        return announcement;
    }

    public List<Announcement> findAll() {
        return announcements.values().stream()
            .sorted(Comparator.comparing(Announcement::getCreatedAt).reversed())
            .collect(Collectors.toList());
    }
}

package com.opengovtbd.repository;

import com.opengovtbd.model.Notification;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class NotificationRepository {

    private final ConcurrentHashMap<Long, Notification> notifications = new ConcurrentHashMap<>();

    public Notification save(Notification notification) {
        notifications.put(notification.getId(), notification);
        return notification;
    }

    public List<Notification> findByUserId(Long userId) {
        return notifications.values().stream()
                .filter(n -> n.getUserId().equals(userId))
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public long countUnread(Long userId) {
        return findByUserId(userId).stream().filter(n -> !n.isRead()).count();
    }
}

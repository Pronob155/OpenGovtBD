package com.opengovtbd.service;

import com.opengovtbd.model.Notification;
import com.opengovtbd.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification notify(Long userId, String message, Notification.Type type, String link) {
        return notificationRepository.save(new Notification(userId, message, type, link));
    }

    public List<Notification> forUser(Long userId) { return notificationRepository.findByUserId(userId); }

    public long unreadCount(Long userId) { return notificationRepository.countUnread(userId); }

    public void markAllRead(Long userId) {
        forUser(userId).forEach(n -> n.setRead(true));
    }

    public void markRead(Long notificationId, Long userId) {
        forUser(userId).stream()
                .filter(n -> n.getId().equals(notificationId))
                .forEach(n -> n.setRead(true));
    }
}

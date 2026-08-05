package com.opengovtbd.service;

import com.opengovtbd.model.Citizen;
import com.opengovtbd.model.Officer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RewardService {
    public static final int POINTS_VOTE = 5;
    public static final int POINTS_SUGGESTION = 15;
    public static final int POINTS_DISCUSSION = 10;
    public static final int POINTS_COMPLAINT = 8;
    public static final int POINTS_COMMENT = 2;
    public static final int POINTS_COMPLAINT_RESOLVED_BONUS = 12;
    public static final int POINTS_COMMUNITY_CONTRIBUTION = 20;

    // Officer performance points (drives the officer leaderboard / activity feed)
    public static final int POINTS_OFFICER_ACCEPT = 5;
    public static final int POINTS_OFFICER_RESOLVE = 15;

    /** A simple activity ledger entry, used to render "Recent Activity" on the leaderboard. */
    public record ActivityEntry(Long userId, String description, int points, LocalDateTime at) {}

    private final Map<Long, Integer> officerPoints = new ConcurrentHashMap<>();
    private final List<ActivityEntry> activityLog = Collections.synchronizedList(new ArrayList<>());

    public void award(Citizen citizen, int points) {
        award(citizen, points, "Civic participation");
    }

    public void award(Citizen citizen, int points, String description) {
        citizen.addPoints(points);
        log(citizen.getId(), description, points);
    }

    public void awardOfficer(Officer officer, int points) {
        officerPoints.merge(officer.getId(), points, Integer::sum);
        log(officer.getId(), officer.getFullName() + " — " + (points == POINTS_OFFICER_ACCEPT ? "accepted a complaint" : "resolved a complaint"), points);
    }

    public int pointsForOfficer(Long officerId) {
        return officerPoints.getOrDefault(officerId, 0);
    }

    private void log(Long userId, String description, int points) {
        activityLog.add(0, new ActivityEntry(userId, description, points, LocalDateTime.now()));
        if (activityLog.size() > 200) activityLog.remove(activityLog.size() - 1);
    }

    public List<ActivityEntry> recentActivity(int limit) {
        synchronized (activityLog) {
            return activityLog.stream().limit(limit).toList();
        }
    }

    public List<ActivityEntry> recentActivityFor(Long userId, int limit) {
        synchronized (activityLog) {
            return activityLog.stream().filter(a -> a.userId().equals(userId)).limit(limit).toList();
        }
    }
}

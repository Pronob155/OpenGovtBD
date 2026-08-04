package com.opengovtbd.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Small shared helper for human-friendly relative timestamps ("5m ago", "3h ago"...). */
final class TimeFormat {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private TimeFormat() {}

    static String relative(LocalDateTime time) {
        if (time == null) return "";
        Duration diff = Duration.between(time, LocalDateTime.now());
        long seconds = Math.max(0, diff.getSeconds());
        if (seconds < 60) return "just now";
        long minutes = seconds / 60;
        if (minutes < 60) return minutes + "m ago";
        long hours = minutes / 60;
        if (hours < 24) return hours + "h ago";
        long days = hours / 24;
        if (days < 7) return days + "d ago";
        if (days < 30) return (days / 7) + "w ago";
        return time.format(DATE_FMT);
    }
}

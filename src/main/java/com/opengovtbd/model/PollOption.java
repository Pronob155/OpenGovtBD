package com.opengovtbd.model;

public class PollOption {
    private final String text;
    private int votes = 0;

    public PollOption(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public int getVotes() {
        return votes;
    }

    public void incrementVotes() {
        this.votes++;
    }
}

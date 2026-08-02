package com.opengovtbd.model;

public enum SuggestionStatus {
    SUBMITTED("Submitted", "info"),
    UNDER_REVIEW("Under Review", "warning"),
    ACCEPTED("Accepted", "success"),
    IMPLEMENTED("Implemented", "success"),
    REJECTED("Rejected", "error");

    private final String label;
    private final String tone;

    SuggestionStatus(String label, String tone) {
        this.label = label;
        this.tone = tone;
    }

    public String getLabel() { return label; }
    public String getTone() { return tone; }
}

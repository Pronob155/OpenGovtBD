package com.opengovtbd.model;

public enum ComplaintStatus {
    SUBMITTED("Submitted", "info"),
    PENDING("Pending", "warning"),
    ASSIGNED("Assigned", "info"),
    UNDER_REVIEW("Under Review", "warning"),
    IN_PROGRESS("In Progress", "warning"),
    WAITING_FOR_CITIZEN("Waiting for Citizen Response", "warning"),
    RESOLVED("Resolved", "success"),
    CLOSED("Closed", "muted"),
    REJECTED("Rejected", "error");

    private final String label;
    private final String tone;

    ComplaintStatus(String label, String tone) {
        this.label = label;
        this.tone = tone;
    }

    public String getLabel() { return label; }
    public String getTone() { return tone; }

    public String getBucket() {
        switch (this) {
            case RESOLVED:
            case CLOSED:
                return "resolved";
            case ASSIGNED:
            case UNDER_REVIEW:
            case IN_PROGRESS:
            case WAITING_FOR_CITIZEN:
                return "inprogress";
            default:
                return "open";
        }
    }
}

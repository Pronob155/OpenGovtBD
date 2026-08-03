package com.opengovtbd.model;

public enum Badge {
    NEW_CITIZEN("New Citizen", 0),
    BRONZE("Bronze Citizen", 50),
    SILVER("Silver Citizen", 150),
    GOLD("Gold Citizen", 400),
    PLATINUM("Platinum Citizen", 1000);

    private final String label;
    private final int minPoints;

    Badge(String label, int minPoints) {
        this.label = label;
        this.minPoints = minPoints;
    }

    public String getLabel() { return label; }
    public int getMinPoints() { return minPoints; }

    public static Badge forPoints(int points) {
        Badge current = NEW_CITIZEN;
        for (Badge b : values()) {
            if (points >= b.minPoints) current = b;
        }
        return current;
    }

    public Badge getNext() {
        Badge[] all = values();
        int nextIndex = ordinal() + 1;
        return nextIndex < all.length ? all[nextIndex] : null;
    }
}

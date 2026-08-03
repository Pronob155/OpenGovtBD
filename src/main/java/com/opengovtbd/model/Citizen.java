package com.opengovtbd.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/** A registered Bangladeshi citizen using the platform. */
public class Citizen extends User {

    private String phone;
    private String secondaryPhone;
    private String email;
    private String nationalId;
    private LocalDate dateOfBirth;
    private String address;
    private String division;
    private boolean phoneVerified;
    private boolean nidVerified;
    private int points;
    private String language = "en"; // "en" or "bn"
    private boolean darkMode = false;
    private final Set<Long> savedPosts = new HashSet<>();
    private final Set<Long> favoriteServices = new HashSet<>();

    // ---- Identity verification (NID + selfie liveness check) ----
    public enum VerificationStatus { NONE, PENDING, VERIFIED, REJECTED }
    private VerificationStatus verificationStatus = VerificationStatus.NONE;
    private String nidImagePath;
    private String selfieImagePath;
    private LocalDateTime verificationSubmittedAt;
    private LocalDateTime verifiedAt;

    public Citizen(String fullName, String phone, String nationalId, LocalDate dob, String password) {
        super(fullName, password);
        this.phone = phone;
        this.nationalId = nationalId;
        this.dateOfBirth = dob;
    }

    @Override
    public String getDashboardUrl() { return "/citizen/dashboard"; }

    @Override
    public String getDisplayRole() { return "Citizen"; }

    @Override
    public Role getRole() { return Role.CITIZEN; }

    @Override
    public String getLoginIdentifier() { return phone; }

    public Badge getBadge() { return Badge.forPoints(points); }

    public void addPoints(int amount) { this.points = Math.max(0, this.points + amount); }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getSecondaryPhone() { return secondaryPhone; }
    public void setSecondaryPhone(String secondaryPhone) { this.secondaryPhone = secondaryPhone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }
    public boolean isPhoneVerified() { return phoneVerified; }
    public void setPhoneVerified(boolean phoneVerified) { this.phoneVerified = phoneVerified; }
    public boolean isNidVerified() { return nidVerified; }
    public void setNidVerified(boolean nidVerified) { this.nidVerified = nidVerified; }

    /** True when the citizen has completed identity verification — drives the blue "Verified" badge app-wide. */
    public boolean isVerified() { return nidVerified && verificationStatus == VerificationStatus.VERIFIED; }

    public int getPoints() { return points; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public boolean isDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }
    public Set<Long> getSavedPosts() { return savedPosts; }
    public Set<Long> getFavoriteServices() { return favoriteServices; }

    public VerificationStatus getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(VerificationStatus verificationStatus) { this.verificationStatus = verificationStatus; }
    public String getNidImagePath() { return nidImagePath; }
    public void setNidImagePath(String nidImagePath) { this.nidImagePath = nidImagePath; }
    public String getSelfieImagePath() { return selfieImagePath; }
    public void setSelfieImagePath(String selfieImagePath) { this.selfieImagePath = selfieImagePath; }
    public LocalDateTime getVerificationSubmittedAt() { return verificationSubmittedAt; }
    public void setVerificationSubmittedAt(LocalDateTime verificationSubmittedAt) { this.verificationSubmittedAt = verificationSubmittedAt; }
    public LocalDateTime getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(LocalDateTime verifiedAt) { this.verifiedAt = verifiedAt; }

    /** Marks identity verification as complete (called after the simulated AI check passes). */
    public void markVerified() {
        this.nidVerified = true;
        this.verificationStatus = VerificationStatus.VERIFIED;
        this.verifiedAt = LocalDateTime.now();
    }

    /** Rough profile completeness used for the dashboard progress ring. */
    public int getProfileCompletion() {
        int total = 8;
        int done = 1; // registered
        if (phoneVerified) done++;
        if (nidVerified) done++;
        if (address != null && !address.isBlank()) done++;
        if (division != null && !division.isBlank()) done++;
        if (dateOfBirth != null) done++;
        if (email != null && !email.isBlank()) done++;
        if (secondaryPhone != null && !secondaryPhone.isBlank()) done++;
        return (int) Math.round((done * 100.0) / total);
    }
}

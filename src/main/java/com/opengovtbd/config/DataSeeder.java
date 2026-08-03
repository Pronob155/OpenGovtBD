package com.opengovtbd.config;

import com.opengovtbd.model.*;
import com.opengovtbd.repository.*;
import com.opengovtbd.service.GovServiceService;
import com.opengovtbd.service.NotificationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ComplaintRepository complaintRepository;
    private final DiscussionRepository discussionRepository;
    private final PollRepository pollRepository;
    private final SuggestionRepository suggestionRepository;
    private final AnnouncementRepository announcementRepository;
    private final NotificationService notificationService;
    private final GovServiceService govServiceService;

    public DataSeeder(UserRepository userRepository, ComplaintRepository complaintRepository,
                       DiscussionRepository discussionRepository, PollRepository pollRepository,
                       SuggestionRepository suggestionRepository, AnnouncementRepository announcementRepository,
                       NotificationService notificationService, GovServiceService govServiceService) {
        this.userRepository = userRepository;
        this.complaintRepository = complaintRepository;
        this.discussionRepository = discussionRepository;
        this.pollRepository = pollRepository;
        this.suggestionRepository = suggestionRepository;
        this.announcementRepository = announcementRepository;
        this.notificationService = notificationService;
        this.govServiceService = govServiceService;
    }

    @Override
    public void run(String... args) {
        // --- Accounts 
        Admin admin = new Admin("Farzana Rahman", "admin@opengovtbd.gov.bd", "admin123");
        userRepository.save(admin);

        Officer officer1 = new Officer("Md. Kamrul Hasan", "OFC-1001", "kamrul.hasan@dhaka.gov.bd",
                "Dhaka City Corporation", "Assistant Engineer", "officer123");
        Officer officer2 = new Officer("Nusrat Jahan", "OFC-1002", "nusrat.jahan@lged.gov.bd",
                "LGED", "Field Officer", "officer123");
        userRepository.save(officer1);
        userRepository.save(officer2);

        // --- Officer-curated digital services ---------------------------
        govServiceService.create("NID Correction Helpdesk", "Fast-track support for correcting name, date of birth, or address on your National ID.",
                "Identity", "badge", "https://services.nidw.gov.bd", officer1);
        govServiceService.create("Union Digital Center Locator", "Find the nearest Union Digital Center for in-person government service support.",
                "Local Government", "location_on", "https://ekpay.gov.bd", officer2);
        govServiceService.create("Online GD (General Diary)", "File a General Diary with your local police station entirely online.",
                "Safety", "local_police", "https://gd.police.gov.bd", officer1);

        Citizen tanvir = new Citizen("Tanvir Ahmed", "01700000000", "1996123456789",
                LocalDate.of(1996, 3, 14), "citizen123");
        tanvir.setPhoneVerified(true);
        tanvir.markVerified();
        tanvir.setAddress("House 12, Road 5, Savar, Dhaka");
        tanvir.setDivision("Dhaka");
        tanvir.addPoints(64);
        userRepository.save(tanvir);

        Citizen mitu = new Citizen("Mitu Sultana", "01812345678", "1998765432109",
                LocalDate.of(1998, 7, 2), "citizen123");
        mitu.setPhoneVerified(true);
        mitu.markVerified();
        mitu.setAddress("Ward 4, Boalia, Rajshahi");
        mitu.setDivision("Rajshahi");
        mitu.addPoints(210);
        userRepository.save(mitu);

        Citizen rakib = new Citizen("Rakibul Islam", "01912223344", "1995556677889",
                LocalDate.of(1995, 11, 20), "citizen123");
        rakib.setPhoneVerified(true);
        rakib.setNidVerified(false);
        rakib.setAddress("Agrabad, Chattogram");
        rakib.setDivision("Chattogram");
        rakib.addPoints(18);
        userRepository.save(rakib);

        // --- Announcements ---------------------------------------------
        announcementRepository.save(new Announcement("Digital Bangladesh Day Celebration",
                "Join nationwide programs celebrating progress in e-governance and digital services on December 12.",
                Announcement.Priority.NORMAL));
        announcementRepository.save(new Announcement("Scheduled Maintenance: Tax Payment Gateway",
                "The online tax payment gateway will be unavailable from 12:00 AM to 4:00 AM for scheduled maintenance.",
                Announcement.Priority.IMPORTANT));
        announcementRepository.save(new Announcement("Heavy Rainfall Alert — Dhaka & Chattogram",
                "The Meteorological Department has issued a heavy rainfall warning. Avoid low-lying areas and follow local advisories.",
                Announcement.Priority.EMERGENCY));

        // --- Complaints ---------------------------------------------
        Complaint c1 = new Complaint("Streetlight not working for 2 weeks",
                "The streetlight near House 12, Road 5 has been off for two weeks, creating safety concerns at night.",
                "Electricity", "Dhaka", "Dhaka", "Savar", tanvir.getId());
        complaintRepository.save(c1);

        Complaint c2 = new Complaint("Overflowing garbage bin near market",
                "The community garbage bin near Savar Bazar has been overflowing for days, causing bad odor.",
                "Waste Management", "Dhaka", "Dhaka", "Savar", tanvir.getId());
        c2.setStatus(ComplaintStatus.IN_PROGRESS);
        c2.setAssignedOfficerId(officer1.getId());
        c2.addTimelineEvent("Assigned to officer", "Handed over to Md. Kamrul Hasan (Dhaka City Corporation)", officer1.getFullName());
        c2.addTimelineEvent("In progress", "Cleanup crew scheduled for this week.", officer1.getFullName());
        complaintRepository.save(c2);

        Complaint c3 = new Complaint("Water supply disruption in Ward 4",
                "No water supply for the last 3 days in Ward 4, Boalia. Please investigate urgently.",
                "Water Supply", "Rajshahi", "Rajshahi", "Boalia", mitu.getId());
        c3.setStatus(ComplaintStatus.RESOLVED);
        c3.setAssignedOfficerId(officer2.getId());
        c3.addTimelineEvent("Assigned to officer", "Handed over to Nusrat Jahan (LGED)", officer2.getFullName());
        c3.addTimelineEvent("Resolved", "Pump station repaired, supply restored.", officer2.getFullName());
        c3.setRating(5);
        c3.setFeedback("Fixed quickly, thank you!");
        complaintRepository.save(c3);

        Complaint c4 = new Complaint("Pothole causing accidents on main road",
                "A large pothole near Agrabad intersection has caused two motorbike accidents this month.",
                "Road & Infrastructure", "Chattogram", "Chattogram", "Agrabad", rakib.getId());
        complaintRepository.save(c4);

        // --- Discussions ---------------------------------------------
        Discussion d1 = new Discussion(mitu.getId(), "Proposal: More solar streetlights in Rajshahi",
                "Given frequent power cuts, solar streetlights could improve safety and reduce cost. What does everyone think?",
                "Infrastructure");
        d1.setApproved(true);
        d1.setPinned(true);
        d1.getLikedBy().addAll(List.of(tanvir.getId(), rakib.getId()));
        d1.setOfficialResponse("Thank you for the proposal. LGED is currently piloting solar streetlights in 3 unions.");
        d1.getComments().add(new Comment(tanvir.getId(), "Great idea, would love to see this in Savar too."));
        discussionRepository.save(d1);

        Discussion d2 = new Discussion(tanvir.getId(), "Traffic congestion near Savar Bus Stand",
                "Traffic has gotten much worse in the mornings. Can we get a traffic officer stationed there during peak hours?",
                "Transportation");
        d2.setApproved(true);
        d2.getLikedBy().add(mitu.getId());
        d2.getComments().add(new Comment(mitu.getId(), "Same issue in Rajshahi near the college gate."));
        discussionRepository.save(d2);

        Discussion d3 = new Discussion(rakib.getId(), "Should mobile courts visit markets more often?",
                "Price manipulation is common during festivals. More frequent mobile court visits could help.",
                "Governance");
        discussionRepository.save(d3); // pending approval

        // --- Polls ---------------------------------------------
        Poll p1 = new Poll("Which service should get a fully digital application process next?", "Digital Services",
                List.of("Trade License", "Land Record Mutation", "Birth Certificate Correction", "Police Clearance"),
                LocalDateTime.now().plusDays(10));
        p1.getOptions().get(0).incrementVotes();
        p1.getOptions().get(0).incrementVotes();
        p1.getOptions().get(1).incrementVotes();
        p1.getOptions().get(2).incrementVotes();
        p1.getOptions().get(2).incrementVotes();
        p1.getOptions().get(2).incrementVotes();
        p1.getVotedCitizens().add(mitu.getId());
        pollRepository.save(p1);

        Poll p2 = new Poll("How satisfied are you with the new online tax payment system?", "Governance",
                List.of("Very satisfied", "Satisfied", "Neutral", "Dissatisfied"),
                LocalDateTime.now().minusDays(3));
        p2.getOptions().get(0).incrementVotes();
        p2.getOptions().get(1).incrementVotes();
        p2.getOptions().get(1).incrementVotes();
        p2.getOptions().get(2).incrementVotes();
        pollRepository.save(p2);

        // --- Suggestions ---------------------------------------------
        Suggestion s1 = new Suggestion(tanvir.getId(), "Add SMS reminders before NID renewal deadline",
                "Many citizens miss NID renewal deadlines. An automated SMS reminder 30 days before expiry would help a lot.");
        s1.setStatus(SuggestionStatus.ACCEPTED);
        s1.setGovernmentFeedback("Great idea — added to the Q3 roadmap for the NID directorate.");
        s1.getUpvotedBy().addAll(List.of(mitu.getId(), rakib.getId()));
        suggestionRepository.save(s1);

        Suggestion s2 = new Suggestion(mitu.getId(), "Bengali keyboard support on all government forms",
                "Several digital forms only accept English input, making them hard to use for many citizens.");
        s2.getUpvotedBy().add(tanvir.getId());
        suggestionRepository.save(s2);

        // --- Notifications ---------------------------------------------
        notificationService.notify(tanvir.getId(), "Your complaint NS-" + LocalDateTime.now().getYear() + "-" + c2.getId()
                + " is now In Progress.", Notification.Type.COMPLAINT, "/citizen/complaints/" + c2.getId());
        notificationService.notify(tanvir.getId(), "New official response on \"Proposal: More solar streetlights in Rajshahi\".",
                Notification.Type.DISCUSSION, "/citizen/discussions/" + d1.getId());
        notificationService.notify(mitu.getId(), "Your suggestion was accepted for the Q3 roadmap!",
                Notification.Type.SUGGESTION, "/citizen/suggestions/" + s1.getId());
        notificationService.notify(tanvir.getId(), "Heavy rainfall alert issued for your area.",
                Notification.Type.EMERGENCY, "/citizen/dashboard");
    }
}

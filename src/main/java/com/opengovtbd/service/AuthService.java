package com.opengovtbd.service;

import com.opengovtbd.model.*;
import com.opengovtbd.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    public static final String DEMO_OTP = "123456";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private final UserRepository userRepository;
    private final NotificationService notificationService;

    /** Registrations awaiting OTP confirmation, keyed by phone number. */
    private final Map<String, Citizen> pendingRegistrations = new ConcurrentHashMap<>();

    public AuthService(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public String beginCitizenRegistration(String fullName, String phone, String nid, LocalDate dob, String password) {
        if (userRepository.findCitizenByPhone(phone).isPresent()) {
            throw new AuthException("This mobile number is already registered.");
        }
        // One National ID can only ever be linked to a single citizen account.
        if (userRepository.findCitizenByNationalId(nid).isPresent()) {
            throw new AuthException("This National ID (NID) is already linked to an existing account. Each NID can only register once.");
        }
        Citizen citizen = new Citizen(fullName, phone, nid, dob, password);
        pendingRegistrations.put(phone, citizen);
        return DEMO_OTP; // In production this would be sent via SMS gateway
    }

    public Citizen confirmOtp(String phone, String otp) {
        Citizen pending = pendingRegistrations.get(phone);
        if (pending == null) throw new AuthException("No pending registration found for this number.");
        if (!DEMO_OTP.equals(otp)) throw new AuthException("Incorrect OTP. Please try again.");
        // Re-check NID uniqueness at confirmation time too, in case of a race between two pending signups.
        if (userRepository.findCitizenByNationalId(pending.getNationalId()).isPresent()) {
            pendingRegistrations.remove(phone);
            throw new AuthException("This National ID (NID) is already linked to an existing account.");
        }
        pending.setPhoneVerified(true);
        pending.addPoints(10);
        userRepository.save(pending);
        pendingRegistrations.remove(phone);
        notificationService.notify(pending.getId(), "Welcome to OpenGovtBD! Your profile has been created.",
                Notification.Type.NOTICE, "/citizen/dashboard");
        return pending;
    }

    public Citizen loginCitizen(String phone, String password) {
        Citizen citizen = userRepository.findCitizenByPhone(phone)
                .orElseThrow(() -> new AuthException("No citizen account found for this mobile number."));
        if (!citizen.getPassword().equals(password)) throw new AuthException("Incorrect password.");
        assertNotBlocked(citizen);
        return citizen;
    }

    public Officer loginOfficer(String officerId, String governmentEmail, String password) {
        Officer officer = userRepository.findOfficerByOfficerId(officerId)
                .orElseThrow(() -> new AuthException("Unknown officer ID."));
        if (!officer.getGovernmentEmail().equalsIgnoreCase(governmentEmail) || !officer.getPassword().equals(password)) {
            throw new AuthException("Officer credentials do not match our records.");
        }
        assertNotBlocked(officer);
        return officer;
    }

    public Admin loginAdmin(String email, String password) {
        Admin admin = userRepository.findAdminByEmail(email)
                .orElseThrow(() -> new AuthException("Unknown admin email."));
        if (!admin.getPassword().equals(password)) throw new AuthException("Incorrect password.");
        assertNotBlocked(admin);
        return admin;
    }

    private void assertNotBlocked(User user) {
        if (user.isBanned()) {
            throw new AuthException("This account has been permanently banned and cannot log in. Contact support if you believe this is a mistake.");
        }
        if (user.isCurrentlySuspended()) {
            throw new AuthException("This account is suspended until " + user.getSuspendedUntil().format(DATE_FMT) + " and cannot log in.");
        }
    }

    public Optional<User> findById(Long id) { return userRepository.findById(id); }
}

package com.opengovtbd.repository;

import com.opengovtbd.model.Admin;
import com.opengovtbd.model.Citizen;
import com.opengovtbd.model.Officer;
import com.opengovtbd.model.User;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class UserRepository {

    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();

    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    public List<Citizen> findAllCitizens() {
        return users.values().stream()
                .filter(Citizen.class::isInstance)
                .map(Citizen.class::cast)
                .sorted(Comparator.comparing(Citizen::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public List<Officer> findAllOfficers() {
        return users.values().stream()
                .filter(Officer.class::isInstance)
                .map(Officer.class::cast)
                .collect(Collectors.toList());
    }

    public Optional<Citizen> findCitizenByPhone(String phone) {
        return findAllCitizens().stream()
                .filter(c -> c.getPhone().equalsIgnoreCase(phone))
                .findFirst();
    }

    /** Used to enforce "one NID = one account" during registration. */
    public Optional<Citizen> findCitizenByNationalId(String nationalId) {
        if (nationalId == null) return Optional.empty();
        return findAllCitizens().stream()
                .filter(c -> c.getNationalId() != null && c.getNationalId().replaceAll("\\s+", "")
                        .equalsIgnoreCase(nationalId.replaceAll("\\s+", "")))
                .findFirst();
    }

    /** Admin "search users by phone number" — supports partial matches. */
    public List<Citizen> searchCitizensByPhone(String query) {
        if (query == null || query.isBlank()) return findAllCitizens();
        String q = query.replaceAll("\\s+", "");
        return findAllCitizens().stream()
                .filter(c -> c.getPhone() != null && c.getPhone().contains(q))
                .collect(Collectors.toList());
    }

    public Optional<Officer> findOfficerByOfficerId(String officerId) {
        return findAllOfficers().stream()
                .filter(o -> o.getOfficerId().equalsIgnoreCase(officerId))
                .findFirst();
    }

    public Optional<Admin> findAdminByEmail(String email) {
        return users.values().stream()
                .filter(Admin.class::isInstance)
                .map(Admin.class::cast)
                .filter(a -> a.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    /** Convenience lookup for templates: never throws, always returns a display name. */
    public String findFullName(Long id) {
        if (id == null) return "Unknown";
        return findById(id).map(User::getFullName).orElse("Unknown Citizen");
    }

    /** Mention/@-autocomplete search across all account types by name or username. */
    public List<User> searchByNameOrUsername(String query, int limit) {
        if (query == null || query.isBlank()) return List.of();
        String q = query.toLowerCase().trim();
        return users.values().stream()
                .filter(u -> u.getFullName().toLowerCase().contains(q) || u.getUsername().toLowerCase().contains(q))
                .sorted(Comparator.comparing(User::getFullName))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Optional<User> findByUsername(String username) {
        if (username == null) return Optional.empty();
        return users.values().stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    public long countCitizens() { return findAllCitizens().size(); }
    public long countVerifiedCitizens() {
        return findAllCitizens().stream().filter(Citizen::isVerified).count();
    }
    public long countSuspendedOrBanned() {
        return users.values().stream().filter(u -> u.isBanned() || u.isCurrentlySuspended()).count();
    }
}

package com.opengovtbd.repository;

import com.opengovtbd.model.Complaint;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class ComplaintRepository {

    private final ConcurrentHashMap<Long, Complaint> complaints = new ConcurrentHashMap<>();

    public Complaint save(Complaint complaint) {
        complaints.put(complaint.getId(), complaint);
        return complaint;
    }

    public Optional<Complaint> findById(Long id) {
        return Optional.ofNullable(complaints.get(id));
    }

    public List<Complaint> findAll() {
        return complaints.values().stream()
                .sorted(Comparator.comparing(Complaint::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public List<Complaint> findByCitizenId(Long citizenId) {
        return findAll().stream()
                .filter(c -> c.getCitizenId().equals(citizenId))
                .collect(Collectors.toList());
    }

    public List<Complaint> findByAssignedOfficerId(Long officerId) {
        return findAll().stream()
                .filter(c -> officerId.equals(c.getAssignedOfficerId()))
                .collect(Collectors.toList());
    }

    public long count() { return complaints.size(); }
    public long countResolved() { return complaints.values().stream().filter(Complaint::isResolved).count(); }
    public long countPending() { return complaints.values().stream().filter(c -> !c.isResolved()).count(); }
}

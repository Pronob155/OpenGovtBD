package com.opengovtbd.repository;

import com.opengovtbd.model.GovService;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class GovServiceRepository {

    private final ConcurrentHashMap<Long, GovService> services = new ConcurrentHashMap<>();

    public GovService save(GovService service) {
        services.put(service.getId(), service);
        return service;
    }

    public Optional<GovService> findById(Long id) {
        return Optional.ofNullable(services.get(id));
    }

    public void deleteById(Long id) {
        services.remove(id);
    }

    public List<GovService> findAll() {
        return services.values().stream()
                .sorted(Comparator.comparing(GovService::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public List<GovService> findByCategory(String category) {
        if (category == null || category.isBlank() || category.equalsIgnoreCase("all"))
            return findAll();
        return findAll().stream().filter(s -> s.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());
    }

    public List<String> categories() {
        return services.values().stream()
                .map(GovService::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public long count() {
        return services.size();
    }
}

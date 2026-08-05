package com.opengovtbd.service;

import com.opengovtbd.model.GovService;
import com.opengovtbd.model.Officer;
import com.opengovtbd.repository.GovServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GovServiceService {

    private final GovServiceRepository repository;

    public GovServiceService(GovServiceRepository repository) {
        this.repository = repository;
    }

    public GovService create(String name, String description, String category, String logoIcon,
            String externalUrl, Officer officer) {
        GovService service = new GovService(name, description, category, logoIcon, externalUrl, officer.getId());
        return repository.save(service);
    }

    public void update(GovService service, String name, String description, String category,
            String logoIcon, String externalUrl) {
        service.setName(name);
        service.setDescription(description);
        service.setCategory(category);
        service.setLogoIcon(logoIcon);
        service.setExternalUrl(externalUrl);
        service.touch();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<GovService> find(Long id) {
        return repository.findById(id);
    }

    public List<GovService> all() {
        return repository.findAll();
    }

    public List<GovService> byCategory(String category) {
        return repository.findByCategory(category);
    }

    public List<String> categories() {
        return repository.categories();
    }

    public long count() {
        return repository.count();
    }
}

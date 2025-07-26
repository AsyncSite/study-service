package com.asyncsite.studyservice.membership.adapter.out.persistence;

import com.asyncsite.studyservice.membership.domain.model.ApplicationForm;
import com.asyncsite.studyservice.membership.domain.port.out.ApplicationFormRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MockApplicationFormRepository implements ApplicationFormRepository {
    
    private final Map<UUID, ApplicationForm> storage = new ConcurrentHashMap<>();
    
    @Override
    public ApplicationForm save(ApplicationForm form) {
        storage.put(form.getId(), form);
        return form;
    }
    
    @Override
    public Optional<ApplicationForm> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public Optional<ApplicationForm> findByStudyIdAndActive(UUID studyId, boolean isActive) {
        return storage.values().stream()
                .filter(form -> form.getStudyId().equals(studyId) && form.isActive() == isActive)
                .findFirst();
    }
    
    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
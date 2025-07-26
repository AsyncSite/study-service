package com.asyncsite.studyservice.membership.adapter.out.persistence;

import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.port.out.ApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class MockApplicationRepository implements ApplicationRepository {
    
    private final Map<UUID, Application> storage = new ConcurrentHashMap<>();
    
    @Override
    public Application save(Application application) {
        storage.put(application.getId(), application);
        return application;
    }
    
    @Override
    public Optional<Application> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<Application> findByStudyId(UUID studyId) {
        return storage.values().stream()
                .filter(app -> app.getStudyId().equals(studyId))
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<Application> findByStudyId(UUID studyId, Pageable pageable) {
        List<Application> applications = findByStudyId(studyId);
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), applications.size());
        
        List<Application> pageContent = applications.subList(start, end);
        return new PageImpl<>(pageContent, pageable, applications.size());
    }
    
    @Override
    public List<Application> findByApplicantId(String applicantId) {
        return storage.values().stream()
                .filter(app -> app.getApplicantId().equals(applicantId))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByStudyIdAndApplicantId(UUID studyId, String applicantId) {
        return storage.values().stream()
                .anyMatch(app -> app.getStudyId().equals(studyId) 
                        && app.getApplicantId().equals(applicantId)
                        && app.isPending());
    }
    
    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
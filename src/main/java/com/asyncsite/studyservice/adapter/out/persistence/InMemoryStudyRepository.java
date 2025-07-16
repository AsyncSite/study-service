package com.asyncsite.studyservice.adapter.out.persistence;

import com.asyncsite.studyservice.domain.model.Study;
import com.asyncsite.studyservice.domain.port.out.StudyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryStudyRepository implements StudyRepository {
    private final Map<UUID, Study> studies = new ConcurrentHashMap<>();

    @Override
    public Study save(final Study study) {
        studies.put(study.getId(), study);
        return study;
    }

    @Override
    public Optional<Study> findById(final UUID id) {
        return Optional.ofNullable(studies.get(id));
    }

    @Override
    public List<Study> findAll() {
        return List.copyOf(studies.values());
    }
    
    @Override
    public Page<Study> findAll(final Pageable pageable) {
        final List<Study> allStudies = List.copyOf(studies.values());
        
        final int start = (int) pageable.getOffset();
        final int end = Math.min(start + pageable.getPageSize(), allStudies.size());
        
        if (start >= allStudies.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, allStudies.size());
        }
        
        final List<Study> pageContent = allStudies.subList(start, end);
        return new PageImpl<>(pageContent, pageable, allStudies.size());
    }

    @Override
    public void deleteById(final UUID id) {
        studies.remove(id);
    }
}
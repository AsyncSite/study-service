package com.asyncsite.studyservice.study.domain.port.out;

import com.asyncsite.studyservice.study.domain.model.Study;
import com.asyncsite.studyservice.study.domain.model.StudyStatus;
import com.asyncsite.studyservice.study.domain.model.StudyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudyRepository {
    Study save(Study study);
    Optional<Study> findById(UUID id);
    Optional<Study> findBySlug(String slug);
    List<Study> findAll();
    Page<Study> findAll(Pageable pageable);
    List<Study> findByStatus(StudyStatus status);
    List<Study> findByType(StudyType type);
    List<Study> findByGeneration(Integer generation);
    List<Study> findByProposerId(String proposerId);
    Page<Study> findByStatus(StudyStatus status, Pageable pageable);
    Page<Study> findByType(StudyType type, Pageable pageable);
    Page<Study> findByGeneration(Integer generation, Pageable pageable);
    boolean existsBySlug(String slug);
    void deleteById(UUID id);
}

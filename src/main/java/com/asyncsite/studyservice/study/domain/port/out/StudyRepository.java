package com.asyncsite.studyservice.study.domain.port.out;

import com.asyncsite.studyservice.study.domain.model.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudyRepository {
    Study save(Study study);
    Optional<Study> findById(UUID id);
    List<Study> findAll();
    Page<Study> findAll(Pageable pageable);
    void deleteById(UUID id);
}

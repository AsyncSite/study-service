package com.asyncsite.studyservice.domain.port.in;

import com.asyncsite.studyservice.domain.model.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudyUseCase {
    Study propose(String title, String description, String proposerId);
    Study approve(UUID studyId);
    Study reject(UUID studyId);
    Study terminate(UUID studyId);
    List<Study> getAllStudies();
    Page<Study> getAllStudies(Pageable pageable);
    Optional<Study> getStudyById(UUID studyId);
}
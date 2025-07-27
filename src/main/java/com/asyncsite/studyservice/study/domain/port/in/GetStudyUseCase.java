package com.asyncsite.studyservice.study.domain.port.in;

import com.asyncsite.studyservice.study.domain.model.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GetStudyUseCase {
    List<Study> getAllStudies();
    Page<Study> getAllStudies(Pageable pageable);
    Optional<Study> getStudyById(UUID studyId);
}

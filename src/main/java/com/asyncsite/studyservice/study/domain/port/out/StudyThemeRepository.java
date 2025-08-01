package com.asyncsite.studyservice.study.domain.port.out;

import com.asyncsite.studyservice.study.domain.model.StudyTheme;

import java.util.Optional;
import java.util.UUID;

public interface StudyThemeRepository {
    StudyTheme save(StudyTheme studyTheme);
    Optional<StudyTheme> findById(UUID id);
    Optional<StudyTheme> findByStudyId(UUID studyId);
    void deleteById(UUID id);
    void deleteByStudyId(UUID studyId);
}

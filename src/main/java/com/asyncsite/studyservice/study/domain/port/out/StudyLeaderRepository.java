package com.asyncsite.studyservice.study.domain.port.out;

import com.asyncsite.studyservice.study.domain.model.StudyLeader;

import java.util.Optional;
import java.util.UUID;

public interface StudyLeaderRepository {
    StudyLeader save(StudyLeader studyLeader);
    Optional<StudyLeader> findById(UUID id);
    Optional<StudyLeader> findByStudyId(UUID studyId);
    void deleteById(UUID id);
    void deleteByStudyId(UUID studyId);
}

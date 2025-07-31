package com.asyncsite.studyservice.study.adapter.out.persistence.repository;

import com.asyncsite.studyservice.study.adapter.out.persistence.entity.StudyLeaderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudyLeaderJpaRepository extends JpaRepository<StudyLeaderJpaEntity, UUID> {
    Optional<StudyLeaderJpaEntity> findByStudyId(UUID studyId);
    void deleteByStudyId(UUID studyId);
}

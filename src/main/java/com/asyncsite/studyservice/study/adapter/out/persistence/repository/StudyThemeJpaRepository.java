package com.asyncsite.studyservice.study.adapter.out.persistence.repository;

import com.asyncsite.studyservice.study.adapter.out.persistence.entity.StudyThemeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudyThemeJpaRepository extends JpaRepository<StudyThemeJpaEntity, UUID> {
    Optional<StudyThemeJpaEntity> findByStudyId(UUID studyId);
    void deleteByStudyId(UUID studyId);
}

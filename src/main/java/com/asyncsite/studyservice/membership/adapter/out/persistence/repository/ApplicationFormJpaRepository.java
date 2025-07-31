package com.asyncsite.studyservice.membership.adapter.out.persistence.repository;

import com.asyncsite.studyservice.membership.adapter.out.persistence.entity.ApplicationFormJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationFormJpaRepository extends JpaRepository<ApplicationFormJpaEntity, UUID> {
    Optional<ApplicationFormJpaEntity> findByStudyIdAndIsActive(UUID studyId, boolean isActive);
}
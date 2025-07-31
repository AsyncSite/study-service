package com.asyncsite.studyservice.membership.adapter.out.persistence.repository;

import com.asyncsite.studyservice.membership.adapter.out.persistence.entity.ApplicationJpaEntity;
import com.asyncsite.studyservice.membership.domain.model.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationJpaRepository extends JpaRepository<ApplicationJpaEntity, UUID> {
    boolean existsByStudyIdAndApplicantIdAndStatus(UUID studyId, String applicantId, ApplicationStatus status);
    List<ApplicationJpaEntity> findByStudyId(UUID studyId);
    List<ApplicationJpaEntity> findByApplicantId(String applicantId);
    Page<ApplicationJpaEntity> findByStudyId(UUID studyId, Pageable pageable);
}
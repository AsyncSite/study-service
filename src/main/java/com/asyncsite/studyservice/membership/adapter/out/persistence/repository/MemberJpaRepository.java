package com.asyncsite.studyservice.membership.adapter.out.persistence.repository;

import com.asyncsite.studyservice.membership.adapter.out.persistence.entity.MemberJpaEntity;
import com.asyncsite.studyservice.membership.domain.model.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, UUID> {
    Page<MemberJpaEntity> findByStudyId(UUID studyId, Pageable pageable);
    Optional<MemberJpaEntity> findByStudyIdAndUserId(UUID studyId, String userId);
    int countByStudyId(UUID studyId);
    int countByStudyIdAndStatus(UUID studyId, MemberStatus status);

    @Query("SELECT m.role, COUNT(m) FROM MemberJpaEntity m WHERE m.studyId = :studyId GROUP BY m.role")
    List<Object[]> countMembersByRole(@Param("studyId") UUID studyId);
    List<MemberJpaEntity> findByUserId(String userId);
}
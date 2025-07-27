package com.asyncsite.studyservice.study.adapter.out.persistence.repository;

import com.asyncsite.studyservice.study.adapter.out.persistence.entity.StudyJpaEntity;
import com.asyncsite.studyservice.study.domain.model.StudyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudyJpaRepository extends JpaRepository<StudyJpaEntity, UUID> {
    
    /**
     * 특정 상태의 스터디들을 조회합니다.
     */
    List<StudyJpaEntity> findByStatus(StudyStatus status);
    
    /**
     * 특정 제안자의 스터디들을 조회합니다.
     */
    List<StudyJpaEntity> findByProposerId(String proposerId);
    
    /**
     * 특정 상태의 스터디들을 페이징하여 조회합니다.
     */
    Page<StudyJpaEntity> findByStatus(StudyStatus status, Pageable pageable);
    
    /**
     * 특정 제안자의 스터디들을 페이징하여 조회합니다.
     */
    Page<StudyJpaEntity> findByProposerId(String proposerId, Pageable pageable);
    
    /**
     * 제목에 특정 키워드가 포함된 스터디들을 조회합니다.
     */
    @Query("SELECT s FROM StudyJpaEntity s WHERE s.title LIKE %:keyword%")
    List<StudyJpaEntity> findByTitleContaining(@Param("keyword") String keyword);
    
    /**
     * 제목 또는 설명에 특정 키워드가 포함된 스터디들을 페이징하여 조회합니다.
     */
    @Query("SELECT s FROM StudyJpaEntity s WHERE s.title LIKE %:keyword% OR s.description LIKE %:keyword%")
    Page<StudyJpaEntity> findByTitleContainingOrDescriptionContaining(@Param("keyword") String keyword, Pageable pageable);
}
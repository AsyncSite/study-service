package com.asyncsite.studyservice.study.adapter.out.persistence;

import com.asyncsite.studyservice.study.adapter.out.persistence.entity.StudyJpaEntity;
import com.asyncsite.studyservice.study.adapter.out.persistence.mapper.StudyPersistenceMapper;
import com.asyncsite.studyservice.study.adapter.out.persistence.repository.StudyJpaRepository;
import com.asyncsite.studyservice.study.domain.model.Study;
import com.asyncsite.studyservice.study.domain.model.StudyStatus;
import com.asyncsite.studyservice.study.domain.model.StudyType;
import com.asyncsite.studyservice.study.domain.port.out.StudyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StudyPersistenceAdapter implements StudyRepository {
    
    private final StudyJpaRepository jpaRepository;
    private final StudyPersistenceMapper mapper;
    
    public StudyPersistenceAdapter(final StudyJpaRepository jpaRepository, 
                                 final StudyPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Study save(final Study study) {
        final Optional<StudyJpaEntity> existingEntity = jpaRepository.findById(study.getId());
        
        final StudyJpaEntity entityToSave;
        if (existingEntity.isPresent()) {
            // 기존 엔티티 업데이트
            entityToSave = mapper.updateEntity(existingEntity.get(), study);
        } else {
            // 새 엔티티 생성
            entityToSave = mapper.toJpaEntity(study);
        }
        
        final StudyJpaEntity savedEntity = jpaRepository.save(entityToSave);
        return mapper.toDomainModel(savedEntity);
    }
    
    @Override
    public Optional<Study> findById(final UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomainModel);
    }
    
    @Override
    public List<Study> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomainModel)
                .toList();
    }
    
    @Override
    public Page<Study> findAll(final Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(mapper::toDomainModel);
    }
    
    @Override
    public void deleteById(final UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<Study> findBySlug(final String slug) {
        return jpaRepository.findBySlug(slug)
                .map(mapper::toDomainModel);
    }
    
    @Override
    public List<Study> findByStatus(final StudyStatus status) {
        return jpaRepository.findByStatus(status)
                .stream()
                .map(mapper::toDomainModel)
                .toList();
    }
    
    @Override
    public List<Study> findByType(final StudyType type) {
        return jpaRepository.findByType(type)
                .stream()
                .map(mapper::toDomainModel)
                .toList();
    }
    
    @Override
    public List<Study> findByGeneration(final Integer generation) {
        return jpaRepository.findByGeneration(generation)
                .stream()
                .map(mapper::toDomainModel)
                .toList();
    }
    
    @Override
    public List<Study> findByProposerId(final String proposerId) {
        return jpaRepository.findByProposerId(proposerId)
                .stream()
                .map(mapper::toDomainModel)
                .toList();
    }
    
    @Override
    public Page<Study> findByStatus(final StudyStatus status, final Pageable pageable) {
        return jpaRepository.findByStatus(status, pageable)
                .map(mapper::toDomainModel);
    }
    
    @Override
    public Page<Study> findByType(final StudyType type, final Pageable pageable) {
        return jpaRepository.findByType(type, pageable)
                .map(mapper::toDomainModel);
    }
    
    @Override
    public Page<Study> findByGeneration(final Integer generation, final Pageable pageable) {
        return jpaRepository.findByGeneration(generation, pageable)
                .map(mapper::toDomainModel);
    }
    
    @Override
    public boolean existsBySlug(final String slug) {
        return jpaRepository.existsBySlug(slug);
    }

    @Override
    public boolean isStudyExists(UUID studyId) {
        return jpaRepository.findById(studyId).isPresent();
    }

    @Override
    public boolean isStudyRecruiting(UUID studyId) {
        return jpaRepository.findById(studyId)
                .map(study -> study.getStatus() == StudyStatus.APPROVED)
                .orElse(false);
    }

    @Override
    public StudyStatus getStudyStatus(UUID studyId) {
        return jpaRepository.findById(studyId)
                .map(mapper::toDomainModel)
                .map(Study::getStatus)
                .orElse(null);
    }

    @Override
    public boolean isUserStudyLeader(UUID studyId, String userId) {
        // Mock implementation - 실제로는 스터디의 리더 정보를 확인해야 함
        return jpaRepository.findById(studyId)
                .map(study -> study.getProposerId().equals(userId))
                .orElse(false);
    }
}
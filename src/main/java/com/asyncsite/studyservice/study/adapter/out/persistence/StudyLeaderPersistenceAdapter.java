package com.asyncsite.studyservice.study.adapter.out.persistence;

import com.asyncsite.studyservice.study.adapter.out.persistence.mapper.StudyLeaderPersistenceMapper;
import com.asyncsite.studyservice.study.adapter.out.persistence.repository.StudyLeaderJpaRepository;
import com.asyncsite.studyservice.study.domain.model.StudyLeader;
import com.asyncsite.studyservice.study.domain.port.out.StudyLeaderRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class StudyLeaderPersistenceAdapter implements StudyLeaderRepository {
    
    private final StudyLeaderJpaRepository jpaRepository;
    private final StudyLeaderPersistenceMapper mapper;
    
    public StudyLeaderPersistenceAdapter(final StudyLeaderJpaRepository jpaRepository,
                                        final StudyLeaderPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public StudyLeader save(final StudyLeader studyLeader) {
        final var entityToSave = mapper.toJpaEntity(studyLeader);
        final var savedEntity = jpaRepository.save(entityToSave);
        return mapper.toDomainModel(savedEntity);
    }
    
    @Override
    public Optional<StudyLeader> findById(final UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomainModel);
    }
    
    @Override
    public Optional<StudyLeader> findByStudyId(final UUID studyId) {
        return jpaRepository.findByStudyId(studyId)
                .map(mapper::toDomainModel);
    }
    
    @Override
    public void deleteById(final UUID id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public void deleteByStudyId(final UUID studyId) {
        jpaRepository.deleteByStudyId(studyId);
    }
}

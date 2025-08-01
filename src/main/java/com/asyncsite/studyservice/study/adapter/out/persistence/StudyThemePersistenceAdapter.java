package com.asyncsite.studyservice.study.adapter.out.persistence;

import com.asyncsite.studyservice.study.adapter.out.persistence.mapper.StudyThemePersistenceMapper;
import com.asyncsite.studyservice.study.adapter.out.persistence.repository.StudyThemeJpaRepository;
import com.asyncsite.studyservice.study.domain.model.StudyTheme;
import com.asyncsite.studyservice.study.domain.port.out.StudyThemeRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class StudyThemePersistenceAdapter implements StudyThemeRepository {
    
    private final StudyThemeJpaRepository jpaRepository;
    private final StudyThemePersistenceMapper mapper;
    
    public StudyThemePersistenceAdapter(final StudyThemeJpaRepository jpaRepository,
                                       final StudyThemePersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public StudyTheme save(final StudyTheme studyTheme) {
        final var entityToSave = mapper.toJpaEntity(studyTheme);
        final var savedEntity = jpaRepository.save(entityToSave);
        return mapper.toDomainModel(savedEntity);
    }
    
    @Override
    public Optional<StudyTheme> findById(final UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomainModel);
    }
    
    @Override
    public Optional<StudyTheme> findByStudyId(final UUID studyId) {
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

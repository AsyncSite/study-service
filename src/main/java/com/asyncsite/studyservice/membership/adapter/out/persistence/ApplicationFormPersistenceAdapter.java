package com.asyncsite.studyservice.membership.adapter.out.persistence;

import com.asyncsite.studyservice.membership.adapter.out.persistence.entity.ApplicationFormJpaEntity;
import com.asyncsite.studyservice.membership.adapter.out.persistence.mapper.ApplicationFormPersistenceMapper;
import com.asyncsite.studyservice.membership.adapter.out.persistence.repository.ApplicationFormJpaRepository;
import com.asyncsite.studyservice.membership.domain.model.ApplicationForm;
import com.asyncsite.studyservice.membership.domain.port.out.ApplicationFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ApplicationFormPersistenceAdapter implements ApplicationFormRepository {

    private final ApplicationFormJpaRepository jpaRepository;
    private final ApplicationFormPersistenceMapper mapper;

    @Override
    public ApplicationForm save(ApplicationForm form) {
        ApplicationFormJpaEntity entity = mapper.toEntity(form);
        ApplicationFormJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ApplicationForm> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<ApplicationForm> findByStudyIdAndActive(UUID studyId, boolean isActive) {
        return jpaRepository.findByStudyIdAndIsActive(studyId, isActive)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
package com.asyncsite.studyservice.membership.adapter.out.persistence;

import com.asyncsite.studyservice.membership.adapter.out.persistence.entity.ApplicationJpaEntity;
import com.asyncsite.studyservice.membership.adapter.out.persistence.mapper.ApplicationPersistenceMapper;
import com.asyncsite.studyservice.membership.adapter.out.persistence.repository.ApplicationJpaRepository;
import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.ApplicationStatus;
import com.asyncsite.studyservice.membership.domain.port.out.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApplicationPersistenceAdapter implements ApplicationRepository {

    private final ApplicationJpaRepository jpaRepository;
    private final ApplicationPersistenceMapper mapper;

    @Override
    public Application save(Application application) {
        ApplicationJpaEntity entity = mapper.toJpaEntity(application);
        ApplicationJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Application> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Application> findByStudyId(UUID studyId) {
        return jpaRepository.findByStudyId(studyId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Application> findByStudyId(UUID studyId, Pageable pageable) {
        return jpaRepository.findByStudyId(studyId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public List<Application> findByApplicantId(String applicantId) {
        return jpaRepository.findByApplicantId(applicantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByStudyIdAndApplicantIdAndStatus(UUID studyId, String applicantId) {
        return jpaRepository.existsByStudyIdAndApplicantIdAndStatus(studyId, applicantId, ApplicationStatus.PENDING);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
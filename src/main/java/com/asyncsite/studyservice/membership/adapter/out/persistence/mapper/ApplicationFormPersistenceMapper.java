package com.asyncsite.studyservice.membership.adapter.out.persistence.mapper;

import com.asyncsite.studyservice.membership.adapter.out.persistence.entity.ApplicationFormJpaEntity;
import com.asyncsite.studyservice.membership.domain.model.ApplicationForm;
import org.springframework.stereotype.Component;

@Component
public class ApplicationFormPersistenceMapper {

    public ApplicationForm toDomain(ApplicationFormJpaEntity entity) {
        return ApplicationForm.builder()
                .id(entity.getId())
                .studyId(entity.getStudyId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .questions(entity.getQuestions())
                .isActive(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    public ApplicationFormJpaEntity toEntity(ApplicationForm domain) {
        return ApplicationFormJpaEntity.builder()
                .id(domain.getId())
                .studyId(domain.getStudyId())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .questions(domain.getQuestions())
                .isActive(domain.isActive())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .build();
    }
}
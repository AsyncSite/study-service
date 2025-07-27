package com.asyncsite.studyservice.study.adapter.out.persistence.mapper;

import com.asyncsite.studyservice.study.adapter.out.persistence.entity.StudyJpaEntity;
import com.asyncsite.studyservice.study.domain.model.Study;
import org.springframework.stereotype.Component;

@Component
public class StudyPersistenceMapper {
    
    /**
     * 도메인 모델을 JPA 엔티티로 변환합니다.
     */
    public StudyJpaEntity toJpaEntity(final Study study) {
        return new StudyJpaEntity(
            study.getId(),
            study.getTitle(),
            study.getDescription(),
            study.getProposerId(),
            study.getStatus(),
            study.getCreatedAt(),
            study.getUpdatedAt()
        );
    }
    
    /**
     * JPA 엔티티를 도메인 모델로 변환합니다.
     */
    public Study toDomainModel(final StudyJpaEntity entity) {
        return new Study(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getProposerId(),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
    
    /**
     * 도메인 모델의 변경사항을 기존 JPA 엔티티에 적용합니다.
     */
    public StudyJpaEntity updateEntity(final StudyJpaEntity entity, final Study study) {
        entity.setStatus(study.getStatus());
        entity.setUpdatedAt(study.getUpdatedAt());
        return entity;
    }
}
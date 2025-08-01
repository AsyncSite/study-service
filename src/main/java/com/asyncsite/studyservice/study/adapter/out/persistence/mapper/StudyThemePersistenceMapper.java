package com.asyncsite.studyservice.study.adapter.out.persistence.mapper;

import com.asyncsite.studyservice.study.adapter.out.persistence.entity.StudyThemeJpaEntity;
import com.asyncsite.studyservice.study.domain.model.StudyTheme;
import org.springframework.stereotype.Component;

@Component
public class StudyThemePersistenceMapper {
    
    /**
     * 도메인 모델을 JPA 엔티티로 변환합니다.
     */
    public StudyThemeJpaEntity toJpaEntity(final StudyTheme studyTheme) {
        return new StudyThemeJpaEntity(
            studyTheme.getId(),
            studyTheme.getStudyId(),
            studyTheme.getPrimaryColor(),
            studyTheme.getSecondaryColor(),
            studyTheme.getGlowColor(),
            studyTheme.getHeroImage()
        );
    }
    
    /**
     * JPA 엔티티를 도메인 모델로 변환합니다.
     */
    public StudyTheme toDomainModel(final StudyThemeJpaEntity entity) {
        return new StudyTheme(
            entity.getId(),
            entity.getStudyId(),
            entity.getPrimaryColor(),
            entity.getSecondaryColor(),
            entity.getGlowColor(),
            entity.getHeroImage()
        );
    }
}

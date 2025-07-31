package com.asyncsite.studyservice.study.adapter.out.persistence.mapper;

import com.asyncsite.studyservice.study.adapter.out.persistence.entity.StudyLeaderJpaEntity;
import com.asyncsite.studyservice.study.domain.model.StudyLeader;
import org.springframework.stereotype.Component;

@Component
public class StudyLeaderPersistenceMapper {
    
    /**
     * 도메인 모델을 JPA 엔티티로 변환합니다.
     */
    public StudyLeaderJpaEntity toJpaEntity(final StudyLeader studyLeader) {
        return new StudyLeaderJpaEntity(
            studyLeader.getId(),
            studyLeader.getStudyId(),
            studyLeader.getName(),
            studyLeader.getProfileImage(),
            studyLeader.getWelcomeMessage(),
            studyLeader.getEmail(),
            studyLeader.getGithubId()
        );
    }
    
    /**
     * JPA 엔티티를 도메인 모델로 변환합니다.
     */
    public StudyLeader toDomainModel(final StudyLeaderJpaEntity entity) {
        return new StudyLeader(
            entity.getId(),
            entity.getStudyId(),
            entity.getName(),
            entity.getProfileImage(),
            entity.getWelcomeMessage(),
            entity.getEmail(),
            entity.getGithubId()
        );
    }
}

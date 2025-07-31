package com.asyncsite.studyservice.membership.adapter.out.persistence.mapper;

import com.asyncsite.studyservice.membership.adapter.out.persistence.entity.MemberJpaEntity;
import com.asyncsite.studyservice.membership.domain.model.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberPersistenceMapper {

    public Member toDomain(MemberJpaEntity entity) {
        return Member.builder()
                .id(entity.getId())
                .studyId(entity.getStudyId())
                .userId(entity.getUserId())
                .role(entity.getRole())
                .status(entity.getStatus())
                .joinedAt(entity.getJoinedAt())
                .lastActiveAt(entity.getLastActiveAt())
                .warningCount(entity.getWarningCount())
                .introduction(entity.getIntroduction())
                .leftAt(entity.getLeftAt())
                .build();
    }

    public MemberJpaEntity toEntity(Member domain) {
        return MemberJpaEntity.builder()
                .id(domain.getId())
                .studyId(domain.getStudyId())
                .userId(domain.getUserId())
                .role(domain.getRole())
                .status(domain.getStatus())
                .joinedAt(domain.getJoinedAt())
                .lastActiveAt(domain.getLastActiveAt())
                .warningCount(domain.getWarningCount())
                .introduction(domain.getIntroduction())
                .leftAt(domain.getLeftAt())
                .build();
    }
}
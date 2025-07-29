package com.asyncsite.studyservice.membership.adapter.out.persistence.mapper;

import com.asyncsite.studyservice.membership.adapter.out.persistence.entity.ApplicationJpaEntity;
import com.asyncsite.studyservice.membership.domain.model.Application;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
public class ApplicationPersistenceMapper {

    private final ObjectMapper objectMapper;

    public ApplicationPersistenceMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ApplicationJpaEntity toJpaEntity(final Application application) {
        return new ApplicationJpaEntity(
                application.getId(),
                application.getStudyId(),
                application.getApplicantId(),
                application.getStatus(),
                application.getAnswers().toString(),
                application.getIntroduction(),
                application.getRejectionReason(),
                application.getAppliedAt(),
                application.getProcessedAt(),
                application.getProcessedBy(),
                application.getReviewNote(),
                application.getReviewedBy(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }

    public Application toDomain(final ApplicationJpaEntity entity) {
        return new Application(
                entity.getId(),
                entity.getStudyId(),
                entity.getApplicantId(),
                entity.getStatus(),
                convertJsonToMap(entity.getAnswers()),
                entity.getIntroduction(),
                entity.getRejectionReason(),
                entity.getAppliedAt(),
                entity.getProcessedAt(),
                entity.getProcessedBy(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getReviewNote(),
                entity.getReviewedBy()
        );
    }

    private Map<String, String> convertJsonToMap(String json) {
        if (json == null || json.isBlank()) return Collections.emptyMap();
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            return Collections.emptyMap();
        }
    }
}

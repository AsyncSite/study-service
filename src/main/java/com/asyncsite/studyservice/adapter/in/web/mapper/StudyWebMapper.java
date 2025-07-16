package com.asyncsite.studyservice.adapter.in.web.mapper;

import com.asyncsite.studyservice.adapter.in.web.StudyResponse;
import com.asyncsite.studyservice.domain.model.Study;
import org.springframework.stereotype.Component;

@Component
public class StudyWebMapper {

    public StudyResponse toResponse(final Study study) {
        return new StudyResponse(
                study.getId(),
                study.getTitle(),
                study.getDescription(),
                study.getProposerId(),
                study.getStatus(),
                study.getCreatedAt(),
                study.getUpdatedAt()
        );
    }
}